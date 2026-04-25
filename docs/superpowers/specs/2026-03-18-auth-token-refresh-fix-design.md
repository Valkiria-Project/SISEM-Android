# Auth Token Refresh Fix — Design Spec

**Date:** 2026-03-18
**Status:** Approved

---

## Problem Summary

The app's OAuth2 token refresh flow has three silent failure modes:

1. **`AccessTokenInterceptor`** — on proactive refresh failure, logs `null` (`BannerModel extends RuntimeException()` with no message arg so `localizedMessage` is always null) and silently keeps the stale token.
2. **`AccessTokenAuthenticator`** — on reactive refresh failure, `runCatching { }.getOrNull()` returns `null`; caller does `newToken?.accessToken.orEmpty()` → signs retry with empty Bearer string → guaranteed 401 loop until `MAX_ATTEMPTS` exhausted; stale token never cleared.
3. **`retryCount` singleton bug** — instance var on `@Singleton`; counts accumulate across concurrent request chains.

`UnauthorizedEventHandler` / `EventBus` / `SisemNavGraph` already wired end-to-end. Every ViewModel uses `publishUnauthorizedEvent(username)` on 401. The interceptors simply don't.

---

## Scope

**Files changed:** 2
- `app/.../data/remote/interceptors/AccessTokenInterceptor.kt`
- `app/.../data/remote/interceptors/AccessTokenAuthenticator.kt`

**Files unchanged:** `AuthRepository`, `AuthRepositoryImpl`, `AuthCacheDataSource`, `UnauthorizedEventHandler`, `EventBus`, `SisemNavGraph`, `NetworkApi`.

**Module co-location:** Both interceptors and `UnauthorizedEventHandler` are in the `:app` module. Kotlin `internal` is module-scoped (not package-scoped), so the call from `com.skgtecnologia.sisem.data.remote.interceptors` to `com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler` compiles correctly.

---

## Design

### 1. `AccessTokenInterceptor` — replace `onFailure` block

In `handleTokenExpirationTime()`, the `onFailure` lambda receives `accessTokenModel` as the `map {}` lambda parameter from `authRepository.getAllAccessTokens().map { accessTokenModel -> ... }`. It is guaranteed non-null: it is an element of the list returned by the suspend function; if the list is empty, the `map` body never executes and `onFailure` is never reached.

**Replace the entire existing `onFailure { ... }` block** (currently: one `storageProvider.storeContent` call logging `throwable.localizedMessage`) **with:**

```kotlin
.onFailure { throwable ->
    val errorMessage = if (throwable is BannerModel) {
        "${throwable.title}: ${throwable.description}"
    } else {
        throwable.message ?: throwable::class.simpleName ?: "UnknownError"
    }

    storageProvider.storeContent(
        ANDROID_NETWORKING_FILE_NAME,
        Context.MODE_APPEND,
        (TimeUtils.getLocalDateTime(Instant.now()).toString() +
            "\t Refreshed Token failure: " + errorMessage +
            "\t using the refresh token: " + accessTokenModel.refreshToken +
            "\n\n").toByteArray()
    )

    authRepository.deleteAccessTokenByUsername(accessTokenModel.username)
    UnauthorizedEventHandler.publishUnauthorizedEvent(accessTokenModel.username)
}
```

The existing `storeContent` call is **replaced** (not supplemented) by the one above, which logs a meaningful error string.

**Ordering:** Deletion and event publication happen before the current request is dispatched (proactive path). The request proceeds with no Authorization header → 401 → caught by `AccessTokenAuthenticator`, which finds no token, returns `null` → `NetworkApi` surfaces re-auth banner. The `SisemNavGraph` observer also navigates to login from the event. If both happen, the second navigation to the already-current login screen is a no-op.

---

### 2. `AccessTokenAuthenticator` — replace `retryCount` with response chain counting

**Remove field:** `private var retryCount = 0`

**Add helper:**

```kotlin
private fun responseCount(response: Response): Int {
    var count = 1
    var prior = response.priorResponse
    while (prior != null) {
        count++
        prior = prior.priorResponse
    }
    return count
}
```

**Update `authenticate()` — the outermost `if` (first line of the method body):**

Before:
```kotlin
return if (response.isUnauthorized() && retryCount < MAX_ATTEMPTS) {
    retryCount++
    // ... token lookup and createSignedRequest ...
} else {
    retryCount = 0
    null
}
```

After:
```kotlin
return if (response.isUnauthorized() && responseCount(response) <= MAX_ATTEMPTS) {
    // ... token lookup and createSignedRequest — unchanged ...
} else {
    null
}
```

Three changes inside this block:
1. `retryCount < MAX_ATTEMPTS` → `responseCount(response) <= MAX_ATTEMPTS`
2. Remove the `retryCount++` line inside the `true` branch
3. Remove the `retryCount = 0` line from the `else` branch (the `else` branch becomes just `null`)

The `synchronized` block and all token-routing `when` logic are inside the `true` branch and are **unchanged**.

---

### 3. `AccessTokenAuthenticator` — `createSignedRequest` failure path

**Before:**

```kotlin
// Returns non-null Request; signs with empty string on failure
private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request =
    synchronized(this) {
        val newToken = runBlocking {
            runCatching {
                authRepository.refreshToken(currentToken = currentToken)
            }.getOrNull()
        }
        request.signWithToken(newToken?.accessToken.orEmpty())
    }
```

**After:**

```kotlin
// Returns Request? — null on refresh failure
private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request? =
    synchronized(this) {
        val newToken = runBlocking {
            runCatching {
                authRepository.refreshToken(currentToken = currentToken)
            }.getOrNull()
        }

        if (newToken == null) {
            runBlocking {
                authRepository.deleteAccessTokenByUsername(currentToken.username)
            }
            UnauthorizedEventHandler.publishUnauthorizedEvent(currentToken.username)
            return@synchronized null
        }

        request.signWithToken(newToken.accessToken)
    }
```

`currentToken` is non-null at this point: it is obtained via the `?.let` call site (`token?.refreshToken?.let { response.createSignedRequest(token) }`), which short-circuits if `token` is null or `token.refreshToken` is null. `createSignedRequest` is never called with a null `currentToken`.

**Call site in `authenticate()` — unchanged:**

```kotlin
token?.refreshToken?.let {
    response.createSignedRequest(token)
}
```

This expression already produces `Request?` (the `.let {}` block returns whatever `createSignedRequest` returns). Changing the return type from `Request` to `Request?` is consistent; the call site compiles without modification.

**Concurrent 401 scenario:** `synchronized(this)` uses the `Response` object as the monitor. Each in-flight request has its own `Response`, so two concurrent 401 responses are NOT serialized by this lock. This is the existing behavior. On concurrent failures: both execute `deleteAccessTokenByUsername` (the second call is a Room no-op on an already-deleted row) and both call `publishUnauthorizedEvent` (the second event navigates to the already-current login screen — no-op). The delete-on-failure path only executes after `refreshToken()` returns null (i.e., after the refresh call completes), so there is no risk of deleting a token while a refresh is still in flight for the same token: deletion happens in the failure branch only after `runCatching` finishes. This concurrent edge case is accepted behavior: both sessions are cleared, user sees the login screen once.

---

## Failure Flow After Fix

```
Token expired (proactive — AccessTokenInterceptor)
  └── refresh() → 400 from Keycloak
        ├── Log: "title: description" (meaningful error message)
        ├── deleteAccessTokenByUsername(username) — clears Room cache
        └── publishUnauthorizedEvent(username)
              └── SisemNavGraph → AuthRoute.LoginRoute ✓

401 received mid-request (reactive — AccessTokenAuthenticator)
  └── responseCount(response) <= MAX_ATTEMPTS?
        ├── yes → resolve token by role → createSignedRequest()
        │     ├── refresh succeeds → signWithToken(newToken.accessToken) → retry ✓
        │     └── refresh fails   → deleteAccessTokenByUsername(username)
        │                            publishUnauthorizedEvent(username)
        │                            return null → OkHttp stops retrying
        │                            401 propagates → NetworkApi re-auth banner ✓
        └── no  → return null → 401 propagates → NetworkApi re-auth banner ✓
```

---

## What Is Not Changed

- **Proactive refresh timing** — existing `LocalDateTime.now() > accessTokenModel.expDate` check preserved
- **Keycloak 400 error body parsing** — out of scope
- **`synchronized` block** — preserved as-is in `createSignedRequest`
- **Multi-role token routing** — `when` block in both interceptors unchanged
- **`AuthRepository` interface** — `deleteAccessTokenByUsername(username)` already exists; no interface change needed
