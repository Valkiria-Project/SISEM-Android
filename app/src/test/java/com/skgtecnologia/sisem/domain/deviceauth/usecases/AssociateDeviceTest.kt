package com.skgtecnologia.sisem.domain.deviceauth.usecases

import com.skgtecnologia.sisem.commons.CODE
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

class AssociateDeviceTest {

    @MockK
    private lateinit var deviceAuthRepository: DeviceAuthRepository

    private lateinit var associateDevice: AssociateDevice

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        associateDevice = AssociateDevice(deviceAuthRepository)
    }

    @Test
    fun `when associateDevice is success`() = runTest {
        coEvery { deviceAuthRepository.associateDevice(any(), any(), any()) } returns mockk()

        val result = associateDevice(SERIAL, CODE, true)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when associateDevice is failure`() = runTest {
        coEvery { deviceAuthRepository.associateDevice(any(), any(), any()) } throws Exception()

        val result = associateDevice(SERIAL, CODE, true)

        Assert.assertEquals(true, result.isFailure)
    }
}
