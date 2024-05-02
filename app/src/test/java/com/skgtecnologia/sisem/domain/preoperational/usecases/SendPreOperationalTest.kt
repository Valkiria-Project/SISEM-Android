package com.skgtecnologia.sisem.domain.preoperational.usecases

import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SendPreOperationalTest {

    @MockK
    private lateinit var preOperationalRepository: PreOperationalRepository

    private lateinit var sendPreOperational: SendPreOperational

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sendPreOperational = SendPreOperational(preOperationalRepository)
    }

    @Test
    fun `when sendPreOperational is success`() = runTest {
        coEvery {
            preOperationalRepository.sendPreOperational(
                ,
                any(),
                any(),
                any(),
                any()
            )
        } returns Unit

        val result = sendPreOperational(
            mapOf(),
            mapOf(),
            mapOf(),
            listOf()
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when sendPreOperational is failure`() = runTest {
        coEvery {
            preOperationalRepository.sendPreOperational(
                ,
                any(),
                any(),
                any(),
                any()
            )
        } throws Exception()

        val result = sendPreOperational(
            mapOf(),
            mapOf(),
            mapOf(),
            listOf()
        )

        Assert.assertEquals(true, result.isFailure)
    }
}
