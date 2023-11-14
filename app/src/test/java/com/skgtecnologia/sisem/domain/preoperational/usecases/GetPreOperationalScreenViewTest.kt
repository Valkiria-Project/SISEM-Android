package com.skgtecnologia.sisem.domain.preoperational.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetPreOperationalScreenViewTest {

    @MockK
    private lateinit var preOperationalRepository: PreOperationalRepository

    private lateinit var getPreOperationalScreenView: GetPreOperationalScreenView

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getPreOperationalScreenView = GetPreOperationalScreenView(preOperationalRepository)
    }

    @Test
    fun `when getPreOperationalViewScreen is success`() = runTest {
        coEvery {
            preOperationalRepository.getPreOperationalViewScreen(
                any(),
                any()
            )
        } returns mockk()

        val result = getPreOperationalScreenView(SERIAL, OperationRole.DRIVER)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getPreOperationalViewScreen is failure`() = runTest {
        coEvery {
            preOperationalRepository.getPreOperationalViewScreen(
                any(),
                any()
            )
        } throws Exception()

        val result = getPreOperationalScreenView(SERIAL, OperationRole.DRIVER)

        Assert.assertEquals(true, result.isFailure)
    }
}
