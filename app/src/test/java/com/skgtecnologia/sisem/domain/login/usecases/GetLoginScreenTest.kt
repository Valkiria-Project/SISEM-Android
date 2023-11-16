package com.skgtecnologia.sisem.domain.login.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.login.LoginRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetLoginScreenTest {

    @MockK
    private lateinit var loginRepository: LoginRepository

    private lateinit var getLoginScreen: GetLoginScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getLoginScreen = GetLoginScreen(loginRepository)
    }

    @Test
    fun `when getLoginScreen is success`() = runTest {
        coEvery { loginRepository.getLoginScreen(any()) } returns mockk()

        val result = getLoginScreen(SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getLoginScreen is failure`() = runTest {
        coEvery { loginRepository.getLoginScreen(any()) } throws Exception()

        val result = getLoginScreen(SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }
}
