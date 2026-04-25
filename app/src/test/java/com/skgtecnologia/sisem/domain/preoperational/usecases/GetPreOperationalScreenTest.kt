package com.skgtecnologia.sisem.domain.preoperational.usecases

import com.skgtecnologia.sisem.commons.DRIVER_ROLE
import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetPreOperationalScreenTest {

    @MockK
    private lateinit var preOperationalRepository: PreOperationalRepository

    private lateinit var getPreOperationalScreen: GetPreOperationalScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getPreOperationalScreen = GetPreOperationalScreen(preOperationalRepository)
    }

    @Test
    fun `when getPreOperationalScreen is success`() = runTest {
        coEvery {
            preOperationalRepository.getPreOperationalScreen(DRIVER_ROLE, any())
        } returns mockk()

        val result = getPreOperationalScreen.invoke(DRIVER_ROLE, SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getPreOperationalScreen is failure`() = runTest {
        coEvery {
            preOperationalRepository.getPreOperationalScreen(
                DRIVER_ROLE,
                any()
            )
        } throws Exception()

        val result = getPreOperationalScreen.invoke(DRIVER_ROLE, SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }
}
