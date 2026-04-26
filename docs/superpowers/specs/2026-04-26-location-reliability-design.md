# Location Sending Reliability & Efficiency — Design Spec

**Date:** 2026-04-26
**Branch:** feature/improve-location-sending
**Scope:** 5 client-side fixes across two categories: delivery reliability and GPS efficiency.

---

## Context

The existing location system has 5 issues identified in `documentation/sisem-location-flow.html`:

**Confiabilidad del envío:**
1. `LocationService` discards the result of `updateLocation.invoke()` — fire-and-forget with no retry on network failure.
2. `LocationService.onStartCommand()` returns `super.onStartCommand()` instead of `START_STICKY` — Android does not guarantee restart after OOM kill.

**Eficiencia GPS:**
3. `FusedLocationProviderClient` callbacks run on `Looper.getMainLooper()` — risks UI jank if GPS processing is slow.
4. `setMinUpdateDistanceMeters(50f)` suppresses updates when the vehicle is stationary — server cannot distinguish "ambulance parked" from "app crashed".
5. `PRIORITY_HIGH_ACCURACY` is always active — drains battery during long standby periods.

---

## Approach

Approach 1 (Incremental/modular): surgical fix per file, no new abstractions beyond `LocationWorker`. Same pattern as the push notification reliability feature. Each change is independent and reviewable.

---

## Architecture

### New file

| File | Responsibility |
|------|---------------|
| `app/src/main/java/com/skgtecnologia/sisem/data/location/worker/LocationWorker.kt` | WorkManager worker that calls `UpdateLocation` use case with retry |

### Modified files

| File | Change |
|------|--------|
| `gradle/libs.versions.toml` | Add `work-runtime-ktx` version |
| `app/build.gradle.kts` | Add `work-runtime-ktx` dependency |
| `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationProvider.kt` | Add `priority: Int` parameter to `getLocationUpdates()` |
| `app/src/main/java/com/skgtecnologia/sisem/commons/location/DefaultLocationProvider.kt` | Accept `priority` param, replace MainLooper with HandlerThread, remove minDistance |
| `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationService.kt` | Enqueue to WorkManager, START_STICKY, adaptive priority with flow restart |

### New test files

| File | Covers |
|------|--------|
| `app/src/test/java/com/skgtecnologia/sisem/data/location/worker/LocationWorkerTest.kt` | WorkManager worker happy path + failure paths |
| `app/src/test/java/com/skgtecnologia/sisem/commons/location/LocationPriorityTest.kt` | Pure `decidePriority()` function |

---

## Implementation Details

### Fix 1 — WorkManager queue

**`gradle/libs.versions.toml`:** add `work-runtime-ktx = "2.10.1"` (latest stable as of April 2026). Also add `hilt-work = "1.2.0"` (Hilt WorkManager integration).

**`app/build.gradle.kts`:** add:
- `implementation(libs.work.runtime.ktx)` — WorkManager core
- `implementation(libs.hilt.work)` — `@HiltWorker` support
- `ksp(libs.hilt.work.compiler)` — annotation processing for HiltWorker
- `testImplementation(libs.work.testing)` — `TestListenableWorkerBuilder` for unit tests

**`di/ApplicationModule.kt` or `SisemApplication.kt`:** WorkManager must be initialized with `HiltWorkerFactory` so Hilt can inject into workers. Add `@HiltAndroidApp` is already present; also need to configure `WorkManager` with the Hilt factory via `Configuration` in `SisemApplication.onCreate()`.

**`LocationWorker.kt`:**
- Extends `CoroutineWorker(context, params)`
- `inputData` keys: `KEY_LATITUDE` (Double), `KEY_LONGITUDE` (Double)
- On `doWork()`: reads lat/lon from inputData, reads vehicleCode from `OperationCacheDataSource` via Hilt injection
- Guard: if vehicleCode is blank → return `Result.failure()` (no point retrying without vehicle identity)
- Calls `updateLocation.invoke(lat, lon)` — on success returns `Result.success()`, on failure returns `Result.retry()` (triggers WorkManager backoff)
- Annotated with `@HiltWorker`, injected with `@AssistedInject`

**`LocationService.kt`:** replace `updateLocation.invoke(location.latitude, location.longitude)` with:
```kotlin
val workRequest = OneTimeWorkRequestBuilder<LocationWorker>()
    .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
    .setInputData(workDataOf(
        LocationWorker.KEY_LATITUDE to location.latitude,
        LocationWorker.KEY_LONGITUDE to location.longitude
    ))
    .build()
WorkManager.getInstance(applicationContext).enqueue(workRequest)
```

### Fix 2 — START_STICKY

In `LocationService.onStartCommand()`, change:
```kotlin
return super.onStartCommand(intent, flags, startId)
```
to:
```kotlin
when (intent?.action) {
    ACTION_START -> start()
    ACTION_STOP -> stop()
}
return START_STICKY
```

### Fix 3 — HandlerThread for GPS callbacks

In `DefaultLocationProvider.getLocationUpdates()`, replace:
```kotlin
client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
```
with:
```kotlin
val handlerThread = HandlerThread("LocationCb").also { it.start() }
client.requestLocationUpdates(request, locationCallback, handlerThread.looper)
awaitClose {
    client.removeLocationUpdates(locationCallback)
    handlerThread.quitSafely()
}
```

### Fix 4 — Remove 50m minimum distance

In `DefaultLocationProvider.getLocationUpdates()`, remove:
```kotlin
.setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
```
and delete the `MIN_UPDATE_DISTANCE_METERS = 50f` constant. FusedLocation will emit every interval regardless of movement.

### Fix 5 — Adaptive priority

**`LocationProvider.kt`:** update interface signature:
```kotlin
fun getLocationUpdates(interval: Long, priority: Int = Priority.PRIORITY_HIGH_ACCURACY): Flow<Location>
```

**`DefaultLocationProvider.kt`:** accept `priority: Int` parameter and use it in `LocationRequest.Builder`.

**`LocationService.kt`:** extract priority decision to a top-level function:
```kotlin
internal fun decidePriority(speed: Float): Int =
    if (speed < STATIONARY_SPEED_THRESHOLD) Priority.PRIORITY_BALANCED_POWER_ACCURACY
    else Priority.PRIORITY_HIGH_ACCURACY

private const val STATIONARY_SPEED_THRESHOLD = 0.5f // m/s (~2 km/h)
```

Track current priority in LocationService and restart collection when it changes:
```kotlin
private var currentPriority = Priority.PRIORITY_HIGH_ACCURACY
private var locationJob: Job? = null

private fun startWithPriority(priority: Int) {
    locationJob?.cancel()
    locationJob = locationProvider
        .getLocationUpdates(LOCATION_INTERVAL, priority)
        .catch { Timber.tag("Location").wtf(it.stackTraceToString()) }
        .onEach { location ->
            // ... existing notification update and storeLocation ...
            enqueueLocationWork(location.latitude, location.longitude)

            val newPriority = decidePriority(location.speed)
            if (newPriority != currentPriority) {
                currentPriority = newPriority
                startWithPriority(newPriority)
            }
        }
        .launchIn(serviceScope)
}
```

---

## Error Handling

| Scenario | Behavior |
|----------|----------|
| No network when sending | WorkManager queues, retries with exponential backoff (15s, 30s, 60s...) |
| Worker fails (non-network error) | `Result.retry()` — WorkManager retries up to default max attempts |
| vehicleCode blank in worker | `Result.failure()` — no retry, notification was already stored in Room |
| Service killed by OOM | `START_STICKY` restarts it; pending WorkManager jobs survive reboot |
| GPS disabled | Existing `LocationException` flow — unchanged |
| Speed unavailable from Location | `location.speed` returns 0.0f when unavailable — treated as stationary, uses balanced priority |

---

## Testing

### `LocationWorkerTest.kt`
Uses `TestListenableWorkerBuilder` (WorkManager's built-in test utility):
- Valid lat/lon + vehicleCode → `UpdateLocation` called → `Result.success()`
- vehicleCode blank → `UpdateLocation` NOT called → `Result.failure()`
- `UpdateLocation` throws → `Result.retry()`

### `LocationPriorityTest.kt`
Pure function tests for `decidePriority()`:
- `speed = 0.0f` → `PRIORITY_BALANCED_POWER_ACCURACY`
- `speed = 0.49f` → `PRIORITY_BALANCED_POWER_ACCURACY`
- `speed = 0.5f` → `PRIORITY_HIGH_ACCURACY`
- `speed = 10.0f` → `PRIORITY_HIGH_ACCURACY`

---

## Out of Scope

- BOOT_COMPLETED BroadcastReceiver (WorkManager already handles reboot persistence)
- Reusing `android_location` local file for batch resubmission (future feature)
- Notification channel differentiation for location service
