package com.skgtecnologia.sisem.data.incident.worker

import androidx.work.ListenableWorker
import com.skgtecnologia.sisem.data.incident.AssignedIncidentFetcher
import com.skgtecnologia.sisem.data.incident.IncidentAssignment
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class IncidentAssignmentRetryTest {

    private val fetcher = mockk<AssignedIncidentFetcher>()
    private val assignment = IncidentAssignment("4521", "HIGH", "-74.1,4.6")
    private val assignedAt = 1_000_000L

    @Test
    fun `the assignment survives the trip through the work data`() {
        assertEquals(assignment, assignment.toData(assignedAt).toAssignment())
    }

    @Test
    fun `once the details load the work is done`() = runTest {
        coEvery { fetcher.fetch(assignment) } returns Result.success(Unit)

        val result = retryAssignment(assignment.toData(assignedAt), fetcher, now = assignedAt + 60_000)

        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `while they do not it keeps trying`() = runTest {
        coEvery { fetcher.fetch(assignment) } returns Result.failure(IOException("weak signal"))

        val result = retryAssignment(assignment.toData(assignedAt), fetcher, now = assignedAt + 60_000)

        assertEquals(ListenableWorker.Result.retry(), result)
    }

    @Test
    fun `an assignment too old is dropped without fetching it`() = runTest {
        val result = retryAssignment(
            assignment.toData(assignedAt),
            fetcher,
            now = assignedAt + MAX_ASSIGNMENT_AGE_MILLIS + 1
        )

        assertEquals(ListenableWorker.Result.failure(), result)
        coVerify(exactly = 0) { fetcher.fetch(any()) }
    }
}
