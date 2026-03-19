# Auth Token Refresh Fix Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix silent token refresh failures so that when a refresh attempt fails, the stale token is cleared from the Room DB and the user is redirected to the login screen via the existing `UnauthorizedEventHandler` event bus.

**Architecture:** Two OkHttp classes are patched — `AccessTokenInterceptor` (proactive expiry check) and `AccessTokenAuthenticator` (reactive 401 handler). Both are `@Singleton`s in the `:app` module with access to `AuthRepository` and the `internal` `UnauthorizedEventHandler`. No new infrastructure is introduced.

**Tech Stack:** Kotlin, OkHttp 4, Hilt, Room, Kotlin Coroutines (`runBlocking`), MockK, JUnit 4, `kotlinx.coroutines.test`

**Spec:** `docs/superpowers/specs/2026-03-18-auth-token-refresh-fix-design.md`

---

## File Map

| Action | File |
|--------|------|
| Modify | `app/src/main/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenInterceptor.kt` |
| Modify | `app/src/main/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenAuthenticator.kt` |
| Create | `app/src/test/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenInterceptorTest.kt` |
| Create | `app/src/test/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenAuthenticatorTest.kt` |

---

## Notes before starting

**`username` source:** Inside the `onFailure` lambda in `handleTokenExpirationTime()`, the username for both `deleteAccessTokenByUsername` and `publishUnauthorizedEvent` must come from `accessTokenModel.username`, where `accessTokenModel` is the `map {}` lambda parameter from `authRepository.getAllAccessTokens().map { accessTokenModel -> ... }`.

**`responseCount` algorithm:** The helper counts the current response plus every response in its `priorResponse` chain:
```kotlin
private fun responseCount(response: Response): Int {
    var count = 1                       // count the current response
    var prior = response.priorResponse
    while (prior != null) {
        count++
        prior = prior.priorResponse
    }
    return count
}
```
A single 401 with no prior responses → `responseCount = 1`. A retry chain of depth 4 (3 priors + current) → `responseCount = 4`. With `MAX_ATTEMPTS = 3` and guard `responseCount(response) <= MAX_ATTEMPTS`, depth-4 returns null (4 > 3 = stop). Depth-1,2,3 proceed.

**`Dispatchers.IO` note:** `UnauthorizedEventHandler.publishUnauthorizedEvent` is not `suspend` — it launches a `CoroutineScope(Dispatchers.IO)` fire-and-forget internally. Tests verify only that the *function was called* (`verify(exactly = 1)`), not internal coroutine completion. `MainDispatcherRule` is not needed to assert the call itself.

**`orEmpty()` removal:** The existing `request.signWithToken(newToken?.accessToken.orEmpty())` line in `createSignedRequest` must be removed entirely and replaced by the explicit null-check + return path. Leaving `orEmpty()` as a fallback would make the null branch unreachable.

---

## Task 1: Tests for `AccessTokenInterceptor` — failure path

**Files:**
- Create: `app/src/test/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenInterceptorTest.kt`

- [ ] **Step 1: Create the test file with failing tests**

```kotlin
package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class AccessTokenInterceptorTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var storageProvider: StorageProvider

    @MockK
    private lateinit var chain: Interceptor.Chain

    private lateinit var interceptor: AccessTokenInterceptor

    private val expiredToken = AccessTokenModel(
        userId = 1,
        dateTime = LocalDateTime.now().minusHours(2),
        accessToken = "old-access-token",
        refreshToken = "old-refresh-token",
        tokenType = "Bearer",
        username = "testuser",
        role = "auxiliary_and_or_taph",
        isAdmin = false,
        nameUser = "Test User",
        preoperational = null,
        turn = null,
        isWarning = false,
        docType = "CC",
        document = "12345",
        refreshDateTime = LocalDateTime.now().minusHours(2),
        expDate = LocalDateTime.now().minusMinutes(10)
    )

    private val freshToken = expiredToken.copy(
        accessToken = "new-access-token",
        refreshToken = "new-refresh-token",
        expDate = LocalDateTime.now().plusMinutes(5),
        refreshDateTime = LocalDateTime.now()
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        interceptor = AccessTokenInterceptor(authRepository, storageProvider)

        every { storageProvider.storeContent(any(), any(), any()) } just runs

        val dummyRequest = Request.Builder().url("https://example.com/api/data").build()
        every { chain.request() } returns dummyRequest
        every { chain.proceed(any()) } returns Response.Builder()
            .request(dummyRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    @Test
    fun `when token is expired and refresh succeeds, no token deletion and no unauthorized event`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } returns freshToken
        coEvery { authRepository.getLastToken() } returns freshToken.accessToken

        interceptor.intercept(chain)

        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `when token is expired and refresh fails with BannerModel, logs title and description, deletes token, publishes unauthorized event`() = runTest {
        val bannerError = BannerModel(
            icon = "ic_alert",
            title = "Token expired",
            description = "Session is no longer valid"
        )
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws bannerError
        coEvery { authRepository.deleteAccessTokenByUsername("testuser") } just runs
        every { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") } just runs

        interceptor.intercept(chain)

        // Verify the logged content contains the BannerModel title, not null
        verify {
            storageProvider.storeContent(
                any(),
                any(),
                match { bytes ->
                    val content = String(bytes)
                    content.contains("Token expired") && content.contains("Session is no longer valid")
                }
            )
        }
        coVerify(exactly = 1) { authRepository.deleteAccessTokenByUsername("testuser") }
        verify(exactly = 1) { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") }
    }

    @Test
    fun `when token is expired and refresh fails with generic exception, deletes token and publishes unauthorized event`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws RuntimeException("network error")
        coEvery { authRepository.deleteAccessTokenByUsername("testuser") } just runs
        every { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") } just runs

        interceptor.intercept(chain)

        coVerify(exactly = 1) { authRepository.deleteAccessTokenByUsername("testuser") }
        verify(exactly = 1) { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") }
    }

    @Test
    fun `when token is not expired, no refresh is attempted`() = runTest {
        val validToken = expiredToken.copy(expDate = LocalDateTime.now().plusMinutes(5))
        coEvery { authRepository.getAllAccessTokens() } returns listOf(validToken)
        coEvery { authRepository.getLastToken() } returns validToken.accessToken

        interceptor.intercept(chain)

        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
    }

    @Test
    fun `when token list is empty, no refresh and no event`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns emptyList()
        coEvery { authRepository.getLastToken() } returns null

        interceptor.intercept(chain)

        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
```

- [ ] **Step 2: Run the tests — expect compile or test failures (implementation not yet changed)**

```bash
cd /Users/jvillada/AndroidStudioProjects/SISEM-Android
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.remote.interceptors.AccessTokenInterceptorTest" 2>&1 | tail -30
```

Expected: failures — tests for the failure path will fail because the current `onFailure` block does not delete the token or publish the event.

---

## Task 2: Fix `AccessTokenInterceptor` — `onFailure` block

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenInterceptor.kt`

- [ ] **Step 3: Add missing imports at the top of the file**

```kotlin
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
```

- [ ] **Step 4: Replace the entire `onFailure` block in `handleTokenExpirationTime()`**

Find the existing `onFailure` block (currently reads `throwable.localizedMessage` — the bug):

```kotlin
// REMOVE this entire block:
.onFailure { throwable ->
    val refreshFailureContent =
        TimeUtils.getLocalDateTime(Instant.now()).toString() +
            "\t Refreshed Token failure: " + throwable.localizedMessage +
            "\t using the refresh token: " + accessTokenModel.refreshToken +
            "\n\n"

    storageProvider.storeContent(
        ANDROID_NETWORKING_FILE_NAME,
        Context.MODE_APPEND,
        refreshFailureContent.toByteArray()
    )
}
```

Replace with (`accessTokenModel.username` is the source for both calls):

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

- [ ] **Step 5: Run the interceptor tests — expect all 5 tests to pass (GREEN)**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.remote.interceptors.AccessTokenInterceptorTest" 2>&1 | tail -20
```

Expected: `BUILD SUCCESSFUL` with 5 tests passing.

- [ ] **Step 6: Commit**

```bash
cd /Users/jvillada/AndroidStudioProjects/SISEM-Android
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add \
  app/src/main/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenInterceptor.kt \
  app/src/test/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenInterceptorTest.kt
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "fix: clear token and force re-login on proactive refresh failure

On refresh failure in AccessTokenInterceptor, delete the stale token
from Room and publish an UnauthorizedSession event so the nav graph
redirects to login instead of silently reusing expired credentials.
Also fixes the null log by reading BannerModel.title/description."
```

---

## Task 3: Tests for `AccessTokenAuthenticator` — `retryCount` and failure path

**Files:**
- Create: `app/src/test/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenAuthenticatorTest.kt`

- [ ] **Step 7: Create the test file with failing tests**

```kotlin
package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class AccessTokenAuthenticatorTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var storageProvider: StorageProvider

    private lateinit var authenticator: AccessTokenAuthenticator

    private val baseRequest = Request.Builder()
        .url("https://example.com/api/data")
        .build()

    private val expiredToken = AccessTokenModel(
        userId = 1,
        dateTime = LocalDateTime.now().minusHours(2),
        accessToken = "old-access-token",
        refreshToken = "old-refresh-token",
        tokenType = "Bearer",
        username = "testuser",
        role = "auxiliary_and_or_taph",
        isAdmin = false,
        nameUser = "Test User",
        preoperational = null,
        turn = null,
        isWarning = false,
        docType = "CC",
        document = "12345",
        refreshDateTime = LocalDateTime.now().minusHours(2),
        expDate = LocalDateTime.now().minusMinutes(10)
    )

    private val freshToken = expiredToken.copy(
        accessToken = "new-access-token",
        refreshToken = "new-refresh-token",
        expDate = LocalDateTime.now().plusMinutes(5),
        refreshDateTime = LocalDateTime.now()
    )

    /**
     * Builds a 401 Response simulating [totalAttemptCount] total attempts.
     *
     * Uses responseCount() semantics: responseCount counts 1 for the current response
     * plus 1 per priorResponse link. build401Response(1) → depth=1, build401Response(4) → depth=4.
     * MAX_ATTEMPTS=3 means depth ≤ 3 proceeds, depth 4 stops.
     */
    private fun build401Response(totalAttemptCount: Int = 1): Response {
        var response = Response.Builder()
            .request(baseRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .build()

        repeat(totalAttemptCount - 1) {
            response = Response.Builder()
                .request(baseRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
                .priorResponse(response)
                .build()
        }
        return response
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        authenticator = AccessTokenAuthenticator(authRepository, storageProvider)

        every { storageProvider.storeContent(any(), any(), any()) } just runs
        every { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) } just runs
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    @Test
    fun `when 401 on first attempt and refresh succeeds, returns request signed with new token`() = runTest {
        val response = build401Response(totalAttemptCount = 1)
        coEvery { authRepository.observeCurrentAccessToken() } returns flowOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } returns freshToken

        val result = authenticator.authenticate(null, response)

        assertNotNull(result)
        assert(result!!.header("Authorization") == "Bearer new-access-token")
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `when 401 on first attempt and refresh fails, deletes token, publishes unauthorized event, returns null`() = runTest {
        val response = build401Response(totalAttemptCount = 1)
        coEvery { authRepository.observeCurrentAccessToken() } returns flowOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws RuntimeException("refresh failed")
        coEvery { authRepository.deleteAccessTokenByUsername("testuser") } just runs

        val result = authenticator.authenticate(null, response)

        // null means OkHttp stops retrying — the 401 propagates to NetworkApi
        assertNull(result)
        coVerify(exactly = 1) { authRepository.deleteAccessTokenByUsername("testuser") }
        verify(exactly = 1) { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") }
    }

    @Test
    fun `when 401 and responseCount exceeds MAX_ATTEMPTS, returns null without refresh`() = runTest {
        // depth=4 → responseCount=4 > MAX_ATTEMPTS=3 → guard fails → return null
        val response = build401Response(totalAttemptCount = 4)

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `when response is not 401, returns null without refresh`() = runTest {
        val response = Response.Builder()
            .request(baseRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(403)
            .message("Forbidden")
            .build()

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
    }

    @Test
    fun `when 401 and no stored token, returns null without refresh or event`() = runTest {
        val response = build401Response(totalAttemptCount = 1)
        coEvery { authRepository.observeCurrentAccessToken() } returns flowOf(null)

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
```

- [ ] **Step 8: Run the tests — expect failures**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.remote.interceptors.AccessTokenAuthenticatorTest" 2>&1 | tail -30
```

Expected: failures on the refresh-failure test (null returned instead of signed empty-token request) and the MAX_ATTEMPTS test (old `retryCount` logic behaves differently).

---

## Task 4: Fix `AccessTokenAuthenticator`

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenAuthenticator.kt`

- [ ] **Step 9: Add missing import**

```kotlin
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
```

- [ ] **Step 10: Remove `retryCount` field and add `responseCount` helper**

Remove line:
```kotlin
private var retryCount = 0
```

Add the helper immediately before `authenticate()`:

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

- [ ] **Step 11: Update the outermost guard in `authenticate()`**

Before:
```kotlin
return if (response.isUnauthorized() && retryCount < MAX_ATTEMPTS) {
    retryCount++
    // ...token lookup...
} else {
    retryCount = 0
    null
}
```

After — three changes: new guard, remove `retryCount++`, replace `retryCount = 0\nnull` with `null`:

```kotlin
return if (response.isUnauthorized() && responseCount(response) <= MAX_ATTEMPTS) {
    // ...token lookup (unchanged)...
} else {
    null
}
```

- [ ] **Step 12: Replace `createSignedRequest` — remove `orEmpty()`, add null-check failure path**

The `orEmpty()` fallback that silently produces an empty Bearer string must be removed entirely.

Before:
```kotlin
private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request =
    synchronized(this) {
        val newToken = runBlocking {
            runCatching {
                authRepository.refreshToken(
                    currentToken = currentToken
                )
            }.getOrNull()
        }

        request.signWithToken(newToken?.accessToken.orEmpty())
    }
```

After:
```kotlin
private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request? =
    synchronized(this) {
        val newToken = runBlocking {
            runCatching {
                authRepository.refreshToken(
                    currentToken = currentToken
                )
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

The call site in `authenticate()` — `token?.refreshToken?.let { response.createSignedRequest(token) }` — does **not** change. It already produces `Request?` and propagates `null` correctly to OkHttp.

- [ ] **Step 13: Run all interceptor tests — expect all 10 to pass (GREEN)**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.remote.interceptors.*" 2>&1 | tail -20
```

Expected: `BUILD SUCCESSFUL`, 10 tests pass (5 interceptor + 5 authenticator).

- [ ] **Step 14: Run the full test suite — check for regressions**

```bash
./gradlew testDebugUnitTest 2>&1 | tail -30
```

Expected: `BUILD SUCCESSFUL`, no regressions.

- [ ] **Step 15: Run ktlint and detekt — fix any issues before committing**

```bash
./gradlew ktlintCheck 2>&1 | tail -20
./gradlew detekt 2>&1 | tail -20
```

If `ktlintCheck` fails, run `./gradlew ktlintFormat` then re-check. Fix any detekt warnings before proceeding.

- [ ] **Step 16: Commit**

```bash
cd /Users/jvillada/AndroidStudioProjects/SISEM-Android
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add \
  app/src/main/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenAuthenticator.kt \
  app/src/test/java/com/skgtecnologia/sisem/data/remote/interceptors/AccessTokenAuthenticatorTest.kt
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "fix: clear token and force re-login on reactive refresh failure

Replace singleton retryCount with per-response chain counting to fix
concurrent-request counting bugs. On refresh failure in
AccessTokenAuthenticator, delete the stale token and publish an
UnauthorizedSession event instead of signing the retry with an empty
Bearer header."
```

---

## Quick Verification Checklist

After both commits, manually verify the fix in a debug build:

- [ ] Force an expired token (set `expDate` to the past via DB Inspector or by waiting for natural expiry)
- [ ] Confirm the `android_networking` log file now shows the actual error (e.g. `"Token expired: Session is no longer valid"`) instead of `"null"`
- [ ] Confirm the user is automatically navigated to the login screen
- [ ] Confirm the stale token is no longer present in the Room DB (check via Android Studio Database Inspector)
