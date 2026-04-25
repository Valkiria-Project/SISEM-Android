package com.skgtecnologia.sisem.domain.forgotpassword.usecases

import com.skgtecnologia.sisem.domain.forgotpassword.ForgotPasswordRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetForgotPasswordScreenTest {

    @MockK
    private lateinit var forgotPasswordRepository: ForgotPasswordRepository

    private lateinit var getForgotPasswordScreen: GetForgotPasswordScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getForgotPasswordScreen = GetForgotPasswordScreen(forgotPasswordRepository)
    }

    @Test
    fun `when getForgotPasswordScreen is success`() = runTest {
        coEvery { forgotPasswordRepository.getForgotPasswordScreen() } returns mockk()

        val result = getForgotPasswordScreen()

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getForgotPasswordScreen is failure`() = runTest {
        coEvery { forgotPasswordRepository.getForgotPasswordScreen() } throws Exception()

        val result = getForgotPasswordScreen()

        Assert.assertEquals(true, result.isFailure)
    }
}
