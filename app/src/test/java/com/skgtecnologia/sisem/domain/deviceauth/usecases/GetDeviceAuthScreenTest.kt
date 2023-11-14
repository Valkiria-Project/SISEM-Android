package com.skgtecnologia.sisem.domain.deviceauth.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.deviceauth.DeviceAuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetDeviceAuthScreenTest {

    @MockK
    private lateinit var deviceAuthRepository: DeviceAuthRepository

    private lateinit var getDeviceAuthScreen: GetDeviceAuthScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getDeviceAuthScreen = GetDeviceAuthScreen(deviceAuthRepository)
    }

    @Test
    fun `when getDeviceAuthScreen is success`() = runTest {
        coEvery { deviceAuthRepository.getDeviceAuthScreen(any()) } returns mockk()

        val result = getDeviceAuthScreen.invoke(SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getDeviceAuthScreen is failure`() = runTest {
        coEvery { deviceAuthRepository.getDeviceAuthScreen(any()) } throws Exception()

        val result = getDeviceAuthScreen.invoke(SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }
}
