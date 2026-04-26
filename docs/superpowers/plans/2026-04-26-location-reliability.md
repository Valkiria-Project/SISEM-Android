# Location Reliability & Efficiency Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace fire-and-forget location HTTP calls with WorkManager retry queue, add GPS efficiency improvements (HandlerThread, adaptive priority, remove 50m filter), and ensure the service restarts automatically after OOM kill.

**Architecture:** 5 surgical fixes across 4 existing files + 1 new file (LocationWorker). WorkManager handles delivery reliability; DefaultLocationProvider handles GPS efficiency; LocationService orchestrates adaptive priority and WorkManager enqueue. No new abstractions beyond LocationWorker.

**Tech Stack:** Kotlin, WorkManager 2.9.1, Hilt WorkManager integration (hilt-work 1.2.0), FusedLocationProviderClient, Coroutines, MockK, JUnit 4.

---

## File Map

| File | Change |
|------|--------|
| `gradle/libs.versions.toml` | Add `work-runtime-ktx`, `hilt-work` versions |
| `app/build.gradle.kts` | Add WorkManager + hilt-work dependencies |
| `app/src/main/AndroidManifest.xml` | Remove default WorkManager auto-initializer |
| `app/src/main/java/com/skgtecnologia/sisem/SisemApplication.kt` | Implement `Configuration.Provider` with HiltWorkerFactory |
| `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationProvider.kt` | Add `priority: Int` param to `getLocationUpdates()` |
| `app/src/main/java/com/skgtecnologia/sisem/commons/location/DefaultLocationProvider.kt` | Accept priority param, HandlerThread, remove minDistance |
| `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationService.kt` | WorkManager enqueue, START_STICKY, adaptive priority |
| `app/src/main/java/com/skgtecnologia/sisem/data/location/worker/LocationWorker.kt` | **NEW** — CoroutineWorker that calls UpdateLocation |
| `app/src/test/java/com/skgtecnologia/sisem/data/location/worker/LocationWorkerTest.kt` | **NEW** — Worker unit tests |
| `app/src/test/java/com/skgtecnologia/sisem/commons/location/LocationPriorityTest.kt` | **NEW** — decidePriority() unit tests |

---

## Setup: Create feature branch

- [ ] **Create and checkout feature branch**

```bash
git checkout main && git pull
git checkout -b feature/improve-location-sending
```

---

## Task 1: Add WorkManager dependencies and configure Hilt integration

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`
- Modify: `app/src/main/AndroidManifest.xml`
- Modify: `app/src/main/java/com/skgtecnologia/sisem/SisemApplication.kt`

> WorkManager requires manual initialization when using `@HiltWorker` so Hilt can inject into workers. This means disabling the auto-initializer in the manifest and providing a custom `Configuration` in `SisemApplication`.

- [ ] **Step 1.1: Add versions to libs.versions.toml**

In `gradle/libs.versions.toml`, add after `ui-viewbinding = "1.10.6"`:

```toml
work-runtime-ktx = "2.9.1"
hilt-work = "1.2.0"
```

Also add these library entries after the `timber` entry in `[libraries]`:

```toml
work-runtime-ktx = { module = "androidx.work:work-runtime-ktx", version.ref = "work-runtime-ktx" }
work-testing = { module = "androidx.work:work-testing", version.ref = "work-runtime-ktx" }
hilt-work = { module = "androidx.hilt:hilt-work", version.ref = "hilt-work" }
```

- [ ] **Step 1.2: Add dependencies to app/build.gradle.kts**

In `app/build.gradle.kts`, find the `// Maps` block and add after it:

```kotlin
// WorkManager
implementation(libs.work.runtime.ktx)
implementation(libs.hilt.work)
testImplementation(libs.work.testing)
```

- [ ] **Step 1.3: Remove default WorkManager auto-initializer from AndroidManifest.xml**

WorkManager auto-initializes itself before Hilt is ready. Disable it by adding this inside `<application>` in `app/src/main/AndroidManifest.xml`:

First, ensure the manifest root has `xmlns:tools="http://schemas.android.com/tools"` — check if it already has it; if not, add it to the `<manifest>` opening tag.

Then add inside `<application>`:

```xml
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    android:exported="false"
    tools:node="merge">
    <meta-data
        android:name="androidx.work.WorkManagerInitializer"
        android:value="androidx.startup"
        tools:node="remove" />
</provider>
```

- [ ] **Step 1.4: Implement Configuration.Provider in SisemApplication.kt**

Replace the full content of `app/src/main/java/com/skgtecnologia/sisem/SisemApplication.kt`:

```kotlin
package com.skgtecnologia.sisem

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SisemApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w(task.exception, "Fetching FCM registration token failed")
                    return@OnCompleteListener
                }
                Timber.d("FCM registration token: ${task.result}")
            }
        )

        MapboxNavigationApp.setup {
            NavigationOptions.Builder(this).build()
        }
    }
}
```

- [ ] **Step 1.5: Build to verify no compile errors**

```bash
./gradlew assembleDebug --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 1.6: Commit**

```bash
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add \
  gradle/libs.versions.toml \
  app/build.gradle.kts \
  app/src/main/AndroidManifest.xml \
  app/src/main/java/com/skgtecnologia/sisem/SisemApplication.kt
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "build: add WorkManager and hilt-work dependencies, configure HiltWorkerFactory"
```

---

## Task 2: Create LocationWorker + tests (TDD)

**Files:**
- Create: `app/src/main/java/com/skgtecnologia/sisem/data/location/worker/LocationWorker.kt`
- Create: `app/src/test/java/com/skgtecnologia/sisem/data/location/worker/LocationWorkerTest.kt`

- [ ] **Step 2.1: Write the failing tests**

Create `app/src/test/java/com/skgtecnologia/sisem/data/location/worker/LocationWorkerTest.kt`:

```kotlin
package com.skgtecnologia.sisem.data.location.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.location.usecases.UpdateLocation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocationWorkerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK private lateinit var updateLocation: UpdateLocation
    @MockK private lateinit var operationCacheDataSource: OperationCacheDataSource

    private lateinit var context: Context

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        context = ApplicationProvider.getApplicationContext()
    }

    private fun buildWorker(lat: Double, lon: Double): LocationWorker {
        return TestListenableWorkerBuilder<LocationWorker>(
            context = context,
            inputData = workDataOf(
                LocationWorker.KEY_LATITUDE to lat,
                LocationWorker.KEY_LONGITUDE to lon
            )
        ).setWorkerFactory(object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker = LocationWorker(
                appContext,
                workerParameters,
                updateLocation,
                operationCacheDataSource
            )
        }).build() as LocationWorker
    }

    @Test
    fun `valid location and vehicleCode returns success`() = runTest {
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flowOf(
            mockk(relaxed = true) { io.mockk.every { vehicleCode } returns "AMB-001" }
        )
        coEvery { updateLocation.invoke(any(), any()) } returns kotlin.Result.success(Unit)

        val result = buildWorker(4.60, -74.08).doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify { updateLocation.invoke(4.60, -74.08) }
    }

    @Test
    fun `blank vehicleCode returns failure without calling updateLocation`() = runTest {
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flowOf(
            mockk(relaxed = true) { io.mockk.every { vehicleCode } returns "" }
        )

        val result = buildWorker(4.60, -74.08).doWork()

        assertEquals(ListenableWorker.Result.failure(), result)
        coVerify(exactly = 0) { updateLocation.invoke(any(), any()) }
    }

    @Test
    fun `updateLocation failure returns retry`() = runTest {
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flowOf(
            mockk(relaxed = true) { io.mockk.every { vehicleCode } returns "AMB-001" }
        )
        coEvery { updateLocation.invoke(any(), any()) } returns kotlin.Result.failure(Throwable("network error"))

        val result = buildWorker(4.60, -74.08).doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }
}
```

- [ ] **Step 2.2: Run tests to verify they fail**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.location.worker.LocationWorkerTest" --console=plain 2>&1 | tail -15
```

Expected: FAIL — `LocationWorker` not found.

- [ ] **Step 2.3: Create LocationWorker**

Create `app/src/main/java/com/skgtecnologia/sisem/data/location/worker/LocationWorker.kt`:

```kotlin
package com.skgtecnologia.sisem.data.location.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.location.usecases.UpdateLocation
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    var updateLocation: UpdateLocation,
    var operationCacheDataSource: OperationCacheDataSource
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val lat = inputData.getDouble(KEY_LATITUDE, 0.0)
        val lon = inputData.getDouble(KEY_LONGITUDE, 0.0)

        val vehicleCode = operationCacheDataSource
            .observeOperationConfig()
            .firstOrNull()
            ?.vehicleCode
            .orEmpty()

        if (vehicleCode.isBlank()) {
            return Result.failure()
        }

        return updateLocation.invoke(lat, lon).fold(
            onSuccess = { ListenableWorker.Result.success() },
            onFailure = { ListenableWorker.Result.retry() }
        )
    }

    companion object {
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
    }
}
```

- [ ] **Step 2.4: Run tests to verify they pass**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.data.location.worker.LocationWorkerTest" --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2.5: Commit**

```bash
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add \
  app/src/main/java/com/skgtecnologia/sisem/data/location/worker/LocationWorker.kt \
  app/src/test/java/com/skgtecnologia/sisem/data/location/worker/LocationWorkerTest.kt
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "feat: add LocationWorker with WorkManager retry for reliable location delivery"
```

---

## Task 3: Extract decidePriority() + tests (TDD)

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationService.kt`
- Create: `app/src/test/java/com/skgtecnologia/sisem/commons/location/LocationPriorityTest.kt`

> Extract `decidePriority()` as an `internal` top-level function in `LocationService.kt` so it can be unit-tested without instantiating the Service.

- [ ] **Step 3.1: Write failing tests**

Create `app/src/test/java/com/skgtecnologia/sisem/commons/location/LocationPriorityTest.kt`:

```kotlin
package com.skgtecnologia.sisem.commons.location

import com.google.android.gms.location.Priority
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationPriorityTest {

    @Test
    fun `speed zero returns balanced power accuracy`() {
        assertEquals(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            decidePriority(0.0f)
        )
    }

    @Test
    fun `speed below threshold returns balanced power accuracy`() {
        assertEquals(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            decidePriority(0.49f)
        )
    }

    @Test
    fun `speed at threshold returns high accuracy`() {
        assertEquals(
            Priority.PRIORITY_HIGH_ACCURACY,
            decidePriority(0.5f)
        )
    }

    @Test
    fun `speed above threshold returns high accuracy`() {
        assertEquals(
            Priority.PRIORITY_HIGH_ACCURACY,
            decidePriority(10.0f)
        )
    }
}
```

- [ ] **Step 3.2: Run tests to verify they fail**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.commons.location.LocationPriorityTest" --console=plain 2>&1 | tail -10
```

Expected: FAIL — `decidePriority` not found.

- [ ] **Step 3.3: Add decidePriority to LocationService.kt**

Add these two declarations at the top of `LocationService.kt`, after the existing `private const val FILE_NAME` constant and before the `@AndroidEntryPoint` annotation:

```kotlin
internal const val STATIONARY_SPEED_THRESHOLD = 0.5f // m/s (~2 km/h)

internal fun decidePriority(speed: Float): Int =
    if (speed < STATIONARY_SPEED_THRESHOLD) Priority.PRIORITY_BALANCED_POWER_ACCURACY
    else Priority.PRIORITY_HIGH_ACCURACY
```

Also add to the imports in `LocationService.kt`:
```kotlin
import com.google.android.gms.location.Priority
```

- [ ] **Step 3.4: Run tests to verify they pass**

```bash
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.commons.location.LocationPriorityTest" --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3.5: Commit**

```bash
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add \
  app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationService.kt \
  app/src/test/java/com/skgtecnologia/sisem/commons/location/LocationPriorityTest.kt
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "feat: add decidePriority() function for adaptive GPS priority based on speed"
```

---

## Task 4: Update LocationProvider interface + DefaultLocationProvider

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationProvider.kt`
- Modify: `app/src/main/java/com/skgtecnologia/sisem/commons/location/DefaultLocationProvider.kt`

- [ ] **Step 4.1: Add priority param to LocationProvider interface**

Replace the full content of `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationProvider.kt`:

```kotlin
package com.skgtecnologia.sisem.commons.location

import android.location.Location
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow

interface LocationProvider {

    fun getLocationUpdates(
        interval: Long,
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY
    ): Flow<Location>

    class LocationException(message: String) : Exception(message)
}
```

- [ ] **Step 4.2: Update DefaultLocationProvider**

Replace the full content of `app/src/main/java/com/skgtecnologia/sisem/commons/location/DefaultLocationProvider.kt`:

```kotlin
package com.skgtecnologia.sisem.commons.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.HandlerThread
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.skgtecnologia.sisem.commons.extensions.hasMapLocationPermission
import com.skgtecnologia.sisem.commons.extensions.validateOrThrow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DefaultLocationProvider(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(
        interval: Long,
        priority: Int
    ): Flow<Location> {
        return callbackFlow {
            validateOrThrow(context.hasMapLocationPermission()) {
                Timber.tag("Location").d("Missing location permissions")
                LocationProvider.LocationException("Missing location permissions")
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            validateOrThrow(isGpsEnabled && isNetworkEnabled) {
                LocationProvider.LocationException("GPS is disabled")
            }

            val request = LocationRequest.Builder(interval)
                .setMinUpdateIntervalMillis(interval)
                .setMaxUpdateDelayMillis(interval)
                .setPriority(priority)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    Timber.tag("Location").d("Location result: ${result.lastLocation}")
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            val handlerThread = HandlerThread("LocationCb").also { it.start() }

            client.requestLocationUpdates(
                request,
                locationCallback,
                handlerThread.looper
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
                handlerThread.quitSafely()
            }
        }
    }
}
```

- [ ] **Step 4.3: Build to verify no compile errors**

```bash
./gradlew assembleDebug --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4.4: Run all unit tests to confirm no regressions**

```bash
./gradlew testDebugUnitTest --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4.5: Commit**

```bash
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add \
  app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationProvider.kt \
  app/src/main/java/com/skgtecnologia/sisem/commons/location/DefaultLocationProvider.kt
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "fix: add HandlerThread for GPS callbacks, remove 50m distance filter, add priority param"
```

---

## Task 5: Update LocationService — WorkManager enqueue, START_STICKY, adaptive priority

**Files:**
- Modify: `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationService.kt`

- [ ] **Step 5.1: Replace LocationService.kt with the full updated version**

Replace the full content of `app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationService.kt`:

```kotlin
package com.skgtecnologia.sisem.commons.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.location.Priority
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.data.location.worker.LocationWorker
import com.valkiria.uicomponents.utlis.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val ACTION_START = "ACTION_START"
const val ACTION_STOP = "ACTION_STOP"
private const val CHANNEL_ID = "location"
private const val CHANNEL_NAME = "Location"
private const val LOCATION_INTERVAL = 5000L
private const val FILE_NAME = "android_location"

internal const val STATIONARY_SPEED_THRESHOLD = 0.5f // m/s (~2 km/h)

internal fun decidePriority(speed: Float): Int =
    if (speed < STATIONARY_SPEED_THRESHOLD) Priority.PRIORITY_BALANCED_POWER_ACCURACY
    else Priority.PRIORITY_HIGH_ACCURACY

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var locationProvider: LocationProvider

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private var locationJob: Job? = null
    private var currentPriority = Priority.PRIORITY_HIGH_ACCURACY

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return START_STICKY
    }

    private fun start() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.location_tracking_title))
            .setContentText(getString(R.string.location_tracking_content))

        startWithPriority(Priority.PRIORITY_HIGH_ACCURACY, notificationManager, notificationBuilder)
        startForeground(1, notificationBuilder.build(), FOREGROUND_SERVICE_TYPE_LOCATION)
    }

    private fun startWithPriority(
        priority: Int,
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder
    ) {
        locationJob?.cancel()
        currentPriority = priority

        locationJob = locationProvider
            .getLocationUpdates(LOCATION_INTERVAL, priority)
            .catch { Timber.tag("Location").wtf(it.stackTraceToString()) }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()

                val updatedNotification = notificationBuilder.setContentText(
                    getString(R.string.location_tracking_content_update, lat, long)
                )
                Timber.tag("Location").d("locationProvider update")
                notificationManager.notify(1, updatedNotification.build())

                storeLocation(lat, long)
                enqueueLocationWork(location.latitude, location.longitude)

                val newPriority = decidePriority(location.speed)
                if (newPriority != currentPriority) {
                    startWithPriority(newPriority, notificationManager, notificationBuilder)
                }
            }
            .launchIn(serviceScope)
    }

    private fun enqueueLocationWork(latitude: Double, longitude: Double) {
        val workRequest = OneTimeWorkRequestBuilder<LocationWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    LocationWorker.KEY_LATITUDE to latitude,
                    LocationWorker.KEY_LONGITUDE to longitude
                )
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }

    private fun storeLocation(lat: String, long: String) {
        val loc = TimeUtils.getLocalTimeAsString() + "\t" + lat + "\t" + long + "\n"
        File(filesDir, FILE_NAME).createNewFile()
        openFileOutput(FILE_NAME, Context.MODE_APPEND).use { output ->
            output.write(loc.toByteArray())
        }
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
```

- [ ] **Step 5.2: Build to verify no compile errors**

```bash
./gradlew assembleDebug --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5.3: Run full unit test suite**

```bash
./gradlew testDebugUnitTest --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5.4: Commit**

```bash
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add \
  app/src/main/java/com/skgtecnologia/sisem/commons/location/LocationService.kt
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "fix: enqueue location updates via WorkManager, add START_STICKY, adaptive GPS priority"
```

---

## Task 6: Final verification and push

- [ ] **Step 6.1: Run ktlint — auto-fix if needed**

```bash
./gradlew ktlintCheck --console=plain 2>&1 | tail -10
```

If it fails:
```bash
./gradlew ktlintFormat --console=plain 2>&1 | tail -5
PRE_COMMIT_ALLOW_NO_CONFIG=1 git add -A
PRE_COMMIT_ALLOW_NO_CONFIG=1 git commit -m "style: apply ktlint formatting"
```

- [ ] **Step 6.2: Run detekt**

```bash
./gradlew detekt --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 6.3: Run full test suite**

```bash
./gradlew testDebugUnitTest --console=plain 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 6.4: Confirm all commits present**

```bash
git log --oneline main..HEAD
```

Expected at least 5 commits:
- `build: add WorkManager and hilt-work dependencies...`
- `feat: add LocationWorker with WorkManager retry...`
- `feat: add decidePriority() function...`
- `fix: add HandlerThread for GPS callbacks, remove 50m distance filter...`
- `fix: enqueue location updates via WorkManager, add START_STICKY...`

- [ ] **Step 6.5: Push to both remotes**

```bash
gh auth switch --user jvillad1
git push origin feature/improve-location-sending
git push gitlab feature/improve-location-sending
```

- [ ] **Step 6.6: Create GitHub PR**

```bash
gh pr create \
  --title "fix: improve location sending reliability and GPS efficiency" \
  --body "Fixes 5 client-side reliability and efficiency issues identified in documentation/sisem-location-flow.html:

- **WorkManager queue**: location updates enqueued with CONNECTED constraint + exponential backoff — zero data loss on network failures
- **START_STICKY**: LocationService restarts automatically after OOM kill
- **HandlerThread**: GPS callbacks moved off MainLooper — no more UI jank risk
- **Remove 50m filter**: location emits every 5s even when stationary — server now gets heartbeat when ambulance is parked
- **Adaptive priority**: switches between HIGH_ACCURACY (moving) and BALANCED_POWER_ACCURACY (stationary) — battery savings during standby

Design spec: docs/superpowers/specs/2026-04-26-location-reliability-design.md
Implementation plan: docs/superpowers/plans/2026-04-26-location-reliability.md" \
  --base main \
  --head feature/improve-location-sending
```

- [ ] **Step 6.7: Create GitLab MR**

```bash
TOKEN=$(git remote get-url gitlab | sed 's|.*://[^:]*:\([^@]*\)@.*|\1|')
curl --silent --request POST \
  --header "PRIVATE-TOKEN: $TOKEN" \
  --header "Content-Type: application/json" \
  --data '{"source_branch":"feature/improve-location-sending","target_branch":"main","title":"fix: improve location sending reliability and GPS efficiency","description":"Fixes 5 client-side issues: WorkManager retry queue, START_STICKY restart, HandlerThread for GPS, remove 50m filter, adaptive GPS priority."}' \
  "https://gitlab.com/api/v4/projects/skg-tecnologia-%2Fsisem%2Fsisem-real%2Fsisem-mobile-application/merge_requests" \
  | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('web_url', d))"
```
