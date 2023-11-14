package com.skgtecnologia.sisem.domain.operation.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetOperationConfigTest {

    @MockK
    private lateinit var operationRepository: OperationRepository

    private lateinit var getOperationConfig: GetOperationConfig

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getOperationConfig = GetOperationConfig(operationRepository)
    }

    @Test
    fun `when getOperationConfig is success`() = runTest {
        coEvery { operationRepository.getOperationConfig(any()) } returns mockk()

        val result = getOperationConfig(SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getOperationConfig is failure`() = runTest {
        coEvery { operationRepository.getOperationConfig(any()) } throws Exception()

        val result = getOperationConfig(SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }
}
