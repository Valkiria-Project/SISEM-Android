package com.skgtecnologia.sisem.commons.communication

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class EventBusTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Test
    fun `when two events are emitted in burst both are received`() = runTest {
        val received = mutableListOf<String>()

        val job = launch {
            EventBus.subscribe<String> { received.add(it) }
        }

        // Let the subscriber coroutine reach the collect call
        testScheduler.advanceUntilIdle()

        EventBus.publish("first")
        EventBus.publish("second")

        testScheduler.advanceUntilIdle()

        assertEquals(listOf("first", "second"), received)
        job.cancel()
    }
}
