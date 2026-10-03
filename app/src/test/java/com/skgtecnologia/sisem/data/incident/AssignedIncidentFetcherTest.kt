package com.skgtecnologia.sisem.data.incident

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.valkiria.uicomponents.components.incident.model.IncidentDetailUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentTypeUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

// Robolectric: IncidentPriority parses its colors with android.graphics.Color.
@RunWith(RobolectricTestRunner::class)
class AssignedIncidentFetcherTest {

    private val incidentCacheDataSource = mockk<IncidentCacheDataSource>(relaxed = true)
    private val incidentRemoteDataSource = mockk<IncidentRemoteDataSource>()
    private val fetcher = AssignedIncidentFetcher(
        authCacheDataSource = mockk<AuthCacheDataSource> { every { observeAccessToken() } returns flowOf(null) },
        incidentCacheDataSource = incidentCacheDataSource,
        incidentRemoteDataSource = incidentRemoteDataSource,
        operationCacheDataSource = mockk<OperationCacheDataSource> {
            every { observeOperationConfig() } returns flowOf(null)
        }
    )

    private val remoteIncident = IncidentUiModel(
        incident = IncidentDetailUiModel(7, "C-7", "S-7", "Calle 1", "", "", "", IncidentTypeUiModel(1, "T"), ""),
        patients = emptyList(),
        resources = emptyList()
    )

    @Test
    fun `the incident is stored with the position from the push`() = runTest {
        coEvery { incidentRemoteDataSource.getIncidentInfo("7", any(), any()) } returns Result.success(remoteIncident)
        val stored = slot<IncidentUiModel>()
        coEvery { incidentCacheDataSource.storeIncident(capture(stored)) } returns Unit

        val result = fetcher.fetch(IncidentAssignment("7", "HIGH", "-74.1, 4.6"))

        assertTrue(result.isSuccess)
        assertEquals(4.6, stored.captured.latitude!!, 0.0)
        assertEquals(-74.1, stored.captured.longitude!!, 0.0)
    }

    @Test
    fun `a malformed position does not lose the incident`() = runTest {
        coEvery { incidentRemoteDataSource.getIncidentInfo("7", any(), any()) } returns Result.success(remoteIncident)
        val stored = slot<IncidentUiModel>()
        coEvery { incidentCacheDataSource.storeIncident(capture(stored)) } returns Unit

        val result = fetcher.fetch(IncidentAssignment("7", "HIGH", ""))

        assertTrue(result.isSuccess)
        assertNull(stored.captured.latitude)
    }

    @Test
    fun `a failed call stores nothing and says so`() = runTest {
        coEvery { incidentRemoteDataSource.getIncidentInfo(any(), any(), any()) } returns
            Result.failure(IOException("weak signal"))

        val result = fetcher.fetch(IncidentAssignment("7", "HIGH", "-74.1,4.6"))

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { incidentCacheDataSource.storeIncident(any()) }
    }
}
