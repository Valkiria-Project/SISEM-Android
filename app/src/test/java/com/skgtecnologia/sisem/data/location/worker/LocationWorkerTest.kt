package com.skgtecnologia.sisem.data.location.worker

import android.content.Context
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
import org.robolectric.RuntimeEnvironment

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
        context = RuntimeEnvironment.getApplication()
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
