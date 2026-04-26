# Push Notification Reliability — Design Spec

**Date:** 2026-04-26
**Branch:** feature/improve-push-notifications
**Scope:** 4 client-side fixes. Token FCM registration (requires backend endpoint) is out of scope and tracked as next step.

---

## Context

The existing push notification system has 4 client-side reliability issues identified in `documentation/sisem-notifications-flow.html`:

1. `POST_NOTIFICATIONS` permission is declared in the manifest but never verified at runtime (Android 13+).
2. `EventBus.subscribe` uses `collectLatest`, which drops earlier notifications when two arrive in quick succession.
3. `MessagingService` uses `Dispatchers.Main` for its coroutine scope, risking ANR if FCM processing exceeds the system's ~10s timeout.
4. `handleIpsPatientTransferredNotification` and `handleTransmiNotification` use `checkNotNull()` without a catch — if the incident cache is empty, the coroutine dies silently.

---

## Approach

Approach 3: Permission check in `SisemNavGraph` + surgical in-place fixes for the other three issues. No new abstractions, no new files. Each fix is independent.

---

## Architecture

### Files changed

| File | Change |
|------|--------|
| `ui/navigation/SisemNavGraph.kt` | Add `POST_NOTIFICATIONS` permission check with banner fallback |
| `commons/communication/EventBus.kt` | `collectLatest` → `collect` |
| `data/notification/MessagingService.kt` | `Dispatchers.Main` → `Dispatchers.IO` |
| `data/notification/NotificationRepositoryImpl.kt` | Replace 2 `checkNotNull()` with explicit null checks |

No new files. No API or data model changes.

---

## Implementation Details

### Fix 1 — POST_NOTIFICATIONS permission (SisemNavGraph.kt)

- Use `rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)` from Accompanist.
- On first composition, if the permission is not granted, launch the permission request.
- If permanently denied, show the existing `BannerComponent` with an action button that deep links to `Settings.ACTION_APPLICATION_DETAILS_SETTINGS`.
- The permission check does **not** block navigation — the app remains fully functional. The banner is informational only.
- Only applies on Android 13+ (API 33). Below that, the permission is granted automatically and no check is needed.

### Fix 2 — EventBus collectLatest → collect (EventBus.kt)

Change line 14 from:
```kotlin
events.filterIsInstance<T>().collectLatest { event -> ... }
```
to:
```kotlin
events.filterIsInstance<T>().collect { event -> ... }
```

With `collect`, each event is fully processed before the next one is handled. Burst notifications (e.g., INCIDENT_ASSIGNED + SUPPORT_REQUEST_ON_THE_WAY arriving simultaneously) will no longer silently drop the first one.

### Fix 3 — Dispatchers.IO in MessagingService (MessagingService.kt)

Change the `serviceScope` from:
```kotlin
CoroutineScope(Dispatchers.Main + serviceJob)
```
to:
```kotlin
CoroutineScope(Dispatchers.IO + serviceJob)
```

This moves the Room write and the remote fetch triggered by `INCIDENT_ASSIGNED` off the main thread, eliminating the ANR risk within FCM's system timeout window.

### Fix 4 — Null safety in NotificationRepositoryImpl (NotificationRepositoryImpl.kt)

Replace `checkNotNull()` calls in both handlers with early-return null checks:

```kotlin
// handleIpsPatientTransferredNotification
val geolocation = notification.geolocation
    ?: return Timber.w("IPS notification missing geolocation, skipping")
val (longitude, latitude) = geolocation.split(",")

val incident = incidentCacheDataSource.observeActiveIncident().first()
    ?: return Timber.w("No active incident for IPS notification, skipping")

// handleTransmiNotification
val incident = incidentCacheDataSource.observeActiveIncident().first()
    ?: return Timber.w("No active incident for Transmi notification, skipping")
```

The notification is already persisted in Room before these handlers run — no data is lost. The function returns gracefully and logs the condition for debugging.

---

## Error Handling

| Scenario | Behavior |
|----------|----------|
| `POST_NOTIFICATIONS` denied | Banner shown, app continues normally |
| `POST_NOTIFICATIONS` permanently denied | Banner with Settings deep link, app continues |
| No active incident on IPS/Transmi notification | Timber.w log, silent return, notification already in Room |
| Burst of notifications on EventBus | All processed in order, none dropped |
| FCM processing takes >10s | Runs on IO thread, no ANR on main thread |

---

## Testing

### EventBus — `EventBusTest.kt`
- Use `TestScope` + `UnconfinedTestDispatcher`
- Emit 2 `NotificationData` events concurrently
- Assert both are received by the subscriber (regression: before fix, only the second arrived)

### MessagingService — `MessagingServiceTest.kt`
- Use MockK to mock `StoreNotification` and `NotificationsManager`
- Verify `storeNotification.invoke()` is called on `Dispatchers.IO`
- Use `StandardTestDispatcher` to control coroutine execution

### NotificationRepositoryImpl — `NotificationRepositoryImplTest.kt`
- `handleIpsPatientTransferredNotification` with null geolocation → no exception thrown, Timber.w called
- `handleIpsPatientTransferredNotification` with null active incident → no exception thrown, Timber.w called
- `handleTransmiNotification` with null active incident → no exception thrown, Timber.w called
- Happy path for both handlers → existing behavior preserved

### Permission check — `SisemNavGraphTest.kt`
- Use `ComposeTestRule` with Accompanist's `FakePermissionState`
- Permission granted → banner not shown
- Permission denied (not permanent) → permission request launched
- Permission permanently denied → Settings banner shown

---

## Out of Scope

- FCM token registration in backend — requires a new backend endpoint. To be implemented as the immediate next feature after this one. Android-side contract: call `RegisterFcmTokenUseCase` in `AuthRepositoryImpl.login()` and in `MessagingService.onNewToken()` once the endpoint is available.
- Notification channels differentiated by type (INCIDENT_ASSIGNED vs. informational)
- Replacing `delay(500)` in `MainActivity` with lifecycle-aware publishing
- FLAG_ONE_SHOT PendingIntent fix
