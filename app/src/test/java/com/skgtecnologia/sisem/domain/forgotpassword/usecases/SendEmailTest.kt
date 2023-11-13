package com.skgtecnologia.sisem.domain.forgotpassword.usecases

import com.skgtecnologia.sisem.commons.EMAIL
import com.skgtecnologia.sisem.domain.forgotpassword.ForgotPasswordRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SendEmailTest {

    @MockK
    private lateinit var forgotPasswordRepository: ForgotPasswordRepository

    private lateinit var sendEmail: SendEmail

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sendEmail = SendEmail(forgotPasswordRepository)
    }

    @Test
    fun `when sendEmail is success`() = runTest {
        coEvery { forgotPasswordRepository.sendEmail(any()) } returns Unit

        val result = sendEmail(EMAIL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when sendEmail is failure`() = runTest {
        coEvery { forgotPasswordRepository.sendEmail(any()) } throws Exception()

        val result = sendEmail(EMAIL)

        Assert.assertEquals(true, result.isFailure)
    }
}
