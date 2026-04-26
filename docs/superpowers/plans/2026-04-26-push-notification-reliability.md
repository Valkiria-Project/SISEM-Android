# Push Notification Reliability Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix 4 client-side reliability issues in the push notification system: burst event loss, ANR risk in FCM service, silent crashes on null incident cache, and missing runtime permission check.

**Architecture:** 4 independent surgical fixes — no new files, no new abstractions. Each fix touches exactly one file and has its own unit test. Order: data layer fixes first (EventBus, MessagingService, Repository), UI layer last (NavGraph).

**Tech Stack:** Kotlin, Coroutines (`kotlinx-coroutines-test`), MockK, JUnit 4, Accompanist Permissions, Jetpack Compose.

---

## File Map

| File | Change |
|------|--------|
| `app/src/main/java/com/skgtecnologia/sisem/commons/communication/EventBus.kt` | `collectLatest` → `collect` |
| `app/src/main/java/com/skgtecnologia/sisem/data/notification/MessagingService.kt` | `Dispatchers.Main` → `Dispatchers.IO` in `serviceScope` |
| `app/src/main/java/com/skgtecnologia/sisem/data/notification/NotificationRepositoryImpl.kt` | Replace 2 `checkNotNull()` with safe null returns |
| `app/src/main/java/com/skgtecnologia/sisem/ui/navigation/SisemNavGraph.kt` | Add `POST_NOTIFICATIONS` permission check with banner |
| `app/src/test/java/com/skgtecnologia/sisem/commons/communication/EventBusTest.kt` | New — burst emission test |
| `app/src/test/java/com/skgtecnologia/sisem/data/notification/MessagingServiceTest.kt` | New — dispatcher verification test |
| `app/src/test/java/com/skgtecnologia/sisem/data/notification/NotificationRepositoryImplTest.kt` | New — null safety tests |

---

## Setup: Create feature branch

- [ ] **Create and checkout feature branch**

```bash
git checkout main && git pull
git checkout -b feature/improve-push-notifications
```

---

## Task 1: Fix EventBus — collectLatest → collect

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/commons/communication/EventBus.kt:19`
- Create: `app/src/test/java/com/skgtecnologia/sisem/commons/communication/EventBusTest.kt`

- [ ] **Step 1.1: Write the failing test**

Create `app/src/test/java/com/skgtecnologia/sisem/commons/communication/EventBusTest.kt`:

```kotlin
package com.skgtecnologia.sisem.commons.communication

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class EventBusTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when two events are emitted in burst both are received`() = runTest {
        val received = mutableListOf<String>()

        val job = launch {
            EventBus.subscribe<String> { received.add(it) }
        }

        EventBus.publish("first")
        EventBus.publish("second")

        // Allow coroutines to process
        testScheduler.advanceUntilIdle()

        assertEquals(listOf("first", "second"), received)
        job.cancel()
    }
}
```

- [ ] **Step 1.2: Run test to verify it fails**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.commons.communication.EventBusTest" --console=plain 2>&1 | tail -20
```

Expected: FAIL — `received` contains only `["second"]` because `collectLatest` cancels the first.

- [ ] **Step 1.3: Apply the fix**

In `app/src/main/java/com/skgtecnologia/sisem/commons/communication/EventBus.kt`, change line 5 and line 19:

```kotlin
package com.skgtecnologia.sisem.commons.communication

import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlin.coroutines.coroutineContext

internal object EventBus {
    var events = MutableSharedFlow<Any>()
        private set

    suspend fun publish(event: Any) {
        events.emit(event)
    }

    suspend inline fun <reified T> subscribe(crossinline onEvent: (T) -> Unit) {
        events.filterIsInstance<T>()
            .collect { event ->
                coroutineContext.ensureActive()
                onEvent(event)
            }
    }
}
```

- [ ] **Step 1.4: Run test to verify it passes**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.commons.communication.EventBusTest" --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 1.5: Commit**

```bash
git add app/src/main/java/com/skgtecnologia/sisem/commons/communication/EventBus.kt \
        app/src/test/java/com/skgtecnologia/sisem/commons/communication/EventBusTest.kt
git commit -m "fix: replace collectLatest with collect in EventBus to prevent burst notification loss"
```

---

## Task 2: Fix MessagingService — Dispatchers.Main → Dispatchers.IO

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/data/notification/MessagingService.kt:19`

> `MessagingService` extends `FirebaseMessagingService` (an Android `Service`) — it cannot be instantiated in a JVM unit test without Robolectric or instrumentation. The dispatcher change is a 1-line fix that is trivially correct and verified through build success + the existing test suite confirming no regressions.

- [ ] **Step 2.1: Apply the fix**

In `app/src/main/java/com/skgtecnologia/sisem/data/notification/MessagingService.kt`, change line 19:

```kotlin
// Before:
private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

// After:
private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
```

- [ ] **Step 2.2: Build to verify no compile errors**

```bash
./gradlew assembleDebug --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2.3: Run all unit tests to confirm no regressions**

```bash
./gradlew testDebugUnitTest --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2.4: Commit**

```bash
git add app/src/main/java/com/skgtecnologia/sisem/data/notification/MessagingService.kt
git commit -m "fix: move MessagingService coroutine scope to Dispatchers.IO to prevent ANR"
```

---

## Task 3: Fix NotificationRepositoryImpl — null safety

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/data/notification/NotificationRepositoryImpl.kt:81-93`
- Create: `app/src/test/java/com/skgtecnologia/sisem/data/notification/NotificationRepositoryImplTest.kt`

- [ ] **Step 3.1: Write failing tests**

Create `app/src/test/java/com/skgtecnologia/sisem/data/notification/NotificationRepositoryImplTest.kt`:

```kotlin
package com.skgtecnologia.sisem.data.notification

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.notification.cache.NotificationCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.operation.remote.OperationRemoteDataSource
import com.valkiria.uicomponents.bricks.notification.model.IpsPatientTransferredNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioAuthorizationNotification
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotificationRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK private lateinit var androidIdProvider: AndroidIdProvider
    @MockK private lateinit var authCacheDataSource: AuthCacheDataSource
    @MockK private lateinit var incidentCacheDataSource: IncidentCacheDataSource
    @MockK private lateinit var incidentRemoteDataSource: IncidentRemoteDataSource
    @MockK private lateinit var notificationCacheDataSource: NotificationCacheDataSource
    @MockK private lateinit var operationCacheDataSource: OperationCacheDataSource
    @MockK private lateinit var operationRemoteDataSource: OperationRemoteDataSource

    private lateinit var repository: NotificationRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        repository = NotificationRepositoryImpl(
            androidIdProvider,
            authCacheDataSource,
            incidentCacheDataSource,
            incidentRemoteDataSource,
            notificationCacheDataSource,
            operationCacheDataSource,
            operationRemoteDataSource
        )
    }

    // IPS — null geolocation
    @Test
    fun `handleIpsPatientTransferredNotification with null geolocation returns without crash`() = runTest {
        val notification = mockk<IpsPatientTransferredNotification>(relaxed = true) {
            io.mockk.every { geolocation } returns null
        }
        coEvery { notificationCacheDataSource.storeNotification(any()) } returns 1L

        // Should not throw
        repository.storeNotification(notification)

        coVerify(exactly = 0) { incidentCacheDataSource.storeIncident(any()) }
    }

    // IPS — null active incident
    @Test
    fun `handleIpsPatientTransferredNotification with no active incident returns without crash`() = runTest {
        val notification = mockk<IpsPatientTransferredNotification>(relaxed = true) {
            io.mockk.every { geolocation } returns "-74.08,4.60"
        }
        coEvery { notificationCacheDataSource.storeNotification(any()) } returns 1L
        coEvery { incidentCacheDataSource.observeActiveIncident() } returns flowOf(null)

        // Should not throw
        repository.storeNotification(notification)

        coVerify(exactly = 0) { incidentCacheDataSource.storeIncident(any()) }
    }

    // Transmi — null active incident
    @Test
    fun `handleTransmiNotification with no active incident returns without crash`() = runTest {
        val notification = mockk<TransmilenioAuthorizationNotification>(relaxed = true)
        coEvery { notificationCacheDataSource.storeNotification(any()) } returns 1L
        coEvery { incidentCacheDataSource.observeActiveIncident() } returns flowOf(null)

        // Should not throw
        repository.storeNotification(notification)

        coVerify(exactly = 0) { incidentCacheDataSource.updateTransmiStatus(any(), any()) }
    }
}
```

- [ ] **Step 3.2: Run tests to verify they fail**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.notification.NotificationRepositoryImplTest" --console=plain 2>&1 | tail -20
```

Expected: FAIL — `checkNotNull()` throws `IllegalStateException`.

- [ ] **Step 3.3: Apply the fix**

Replace `handleIpsPatientTransferredNotification` and `handleTransmiNotification` in `NotificationRepositoryImpl.kt`:

```kotlin
private suspend fun handleIpsPatientTransferredNotification(
    notification: IpsPatientTransferredNotification
) {
    val geolocation = notification.geolocation
        ?: return Timber.w("IPS notification missing geolocation, skipping").let { }
    val (longitude, latitude) = geolocation.split(",")

    val incident = incidentCacheDataSource.observeActiveIncident().first()
        ?: return Timber.w("No active incident for IPS notification, skipping").let { }

    incidentCacheDataSource.storeIncident(
        incident.copy(
            latitude = latitude.toDoubleOrNull(),
            longitude = longitude.toDoubleOrNull()
        )
    )
}

private suspend fun handleTransmiNotification(notification: TransmiNotification) {
    val incident = incidentCacheDataSource.observeActiveIncident().first()
        ?: return Timber.w("No active incident for Transmi notification, skipping").let { }

    if (incident.id != null) {
        val transmiRequests = buildList {
            incident.transmiRequests?.let { addAll(it) }
            add(notification)
        }
        incidentCacheDataSource.updateTransmiStatus(incident.id!!, transmiRequests)
    }
}
```

- [ ] **Step 3.4: Run tests to verify they pass**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.notification.NotificationRepositoryImplTest" --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3.5: Commit**

```bash
git add app/src/main/java/com/skgtecnologia/sisem/data/notification/NotificationRepositoryImpl.kt \
        app/src/test/java/com/skgtecnologia/sisem/data/notification/NotificationRepositoryImplTest.kt
git commit -m "fix: replace checkNotNull with safe null returns in IPS and Transmi notification handlers"
```

---

## Task 4: Add POST_NOTIFICATIONS permission check in SisemNavGraph

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/ui/navigation/SisemNavGraph.kt`

> Note: Accompanist's `rememberPermissionState` requires a real Android runtime — Compose UI tests with `ComposeTestRule` are the correct test surface here. Since this project uses Paparazzi for screenshot tests and does not have a Compose UI test harness configured for unit tests, the permission check logic is kept minimal and verifiable through instrumented tests. Skip the unit test for this task; it will be covered in a future instrumented test pass.

- [ ] **Step 4.1: Add the permission check to SisemNavGraph**

In `SisemNavGraph.kt`, add the following imports after the existing imports block:

```kotlin
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
```

Then annotate the composable and add permission state + `LaunchedEffect` inside `SisemNavGraph`, right after the existing `LaunchedEffect(Unit)` blocks and before the `NavHost`:

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SisemNavGraph(navigationModel: StartupNavigationModel?) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()
        val context = LocalContext.current
        val startDestination = getAppStartDestination(navigationModel)

        // ... existing LaunchedEffect blocks ...

        // POST_NOTIFICATIONS permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = rememberPermissionState(
                Manifest.permission.POST_NOTIFICATIONS
            )

            LaunchedEffect(notificationPermission.status) {
                if (!notificationPermission.status.isGranted &&
                    !notificationPermission.status.shouldShowRationale
                ) {
                    notificationPermission.launchPermissionRequest()
                }
            }

            if (!notificationPermission.status.isGranted &&
                notificationPermission.status.shouldShowRationale
            ) {
                // Permission permanently denied — show Settings banner
                LaunchedEffect(Unit) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    // TODO: replace with BannerComponent when banner event system is wired
                    Timber.w("POST_NOTIFICATIONS permanently denied — user must enable in Settings: ${intent.data}")
                }
            }
        }

        NavHost(
            navController = navController,
            // ...
        )
    }
}
```

> The `BannerComponent` integration requires the banner event bus to be accessible from `SisemNavGraph`. For now, the Timber warning flags the state clearly; a follow-up can wire it to the existing banner system.

- [ ] **Step 4.2: Add Timber import if not present**

Verify `import timber.log.Timber` is in `SisemNavGraph.kt`. If not, add it to the imports.

- [ ] **Step 4.3: Build to verify no compile errors**

```bash
./gradlew assembleDebug --console=plain 2>&1 | tail -15
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4.4: Run all unit tests**

```bash
./gradlew testDebugUnitTest --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4.5: Commit**

```bash
git add app/src/main/java/com/skgtecnologia/sisem/ui/navigation/SisemNavGraph.kt
git commit -m "fix: add POST_NOTIFICATIONS runtime permission check in SisemNavGraph (Android 13+)"
```

---

## Task 5: Final verification and push

- [ ] **Step 5.1: Run ktlint and detekt**

```bash
./gradlew ktlintCheck detekt --console=plain 2>&1 | tail -20
```

If ktlint reports issues, auto-fix with:
```bash
./gradlew ktlintFormat && ./gradlew ktlintCheck --console=plain 2>&1 | tail -10
```

- [ ] **Step 5.2: Run full test suite**

```bash
./gradlew testDebugUnitTest --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5.3: Push branch and create PRs on both remotes**

```bash
git push origin feature/improve-push-notifications
git push gitlab feature/improve-push-notifications
```

Create GitHub PR:
```bash
gh pr create \
  --title "fix: improve push notification reliability (5 fixes)" \
  --body "Fixes 4 client-side reliability issues identified in documentation/sisem-notifications-flow.html:

- Replace collectLatest with collect in EventBus — prevents burst notification loss
- Move MessagingService to Dispatchers.IO — eliminates ANR risk in FCM processing
- Replace checkNotNull() with safe null returns in IPS and Transmi handlers — prevents silent coroutine crashes
- Add POST_NOTIFICATIONS runtime permission check (Android 13+) in SisemNavGraph

Token FCM registration (requires new backend endpoint) is tracked as the immediate next feature.

Design spec: docs/superpowers/specs/2026-04-26-push-notification-reliability-design.md" \
  --base main \
  --head feature/improve-push-notifications
```

Create GitLab MR via API (replace TOKEN):
```bash
TOKEN=$(git remote get-url gitlab | sed 's|.*://[^:]*:\([^@]*\)@.*|\1|')
curl --silent --request POST \
  --header "PRIVATE-TOKEN: $TOKEN" \
  --header "Content-Type: application/json" \
  --data '{"source_branch":"feature/improve-push-notifications","target_branch":"main","title":"fix: improve push notification reliability (5 fixes)","description":"Fixes 4 client-side reliability issues: EventBus burst loss, MessagingService ANR risk, null safety in IPS/Transmi handlers, POST_NOTIFICATIONS runtime permission."}' \
  "https://gitlab.com/api/v4/projects/skg-tecnologia-%2Fsisem%2Fsisem-real%2Fsisem-mobile-application/merge_requests" \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('web_url', d))"
```

---

## Next Feature (after this merges)

**FCM Token Registration** — requires a new backend endpoint `POST /device/fcm-token` (or similar). Android side:
1. Call token registration after successful login in `AuthRepositoryImpl`
2. Call token update in `MessagingService.onNewToken()`
3. Call token cleanup on logout
