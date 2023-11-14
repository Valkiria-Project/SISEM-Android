package com.skgtecnologia.sisem.domain.changepassword.usecases

import com.skgtecnologia.sisem.commons.PASSWORD
import com.skgtecnologia.sisem.domain.changepassword.ChangePasswordRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChangePasswordTest {

    @MockK
    private lateinit var changePasswordRepository: ChangePasswordRepository

    private lateinit var changePassword: ChangePassword

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        changePassword = ChangePassword(changePasswordRepository)
    }

    @Test
    fun `when changePassword is success`() = runTest {
        coEvery { changePasswordRepository.changePassword(any(), any()) } returns ""

        val result = changePassword(PASSWORD, PASSWORD)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when changePassword is failure`() = runTest {
        coEvery { changePasswordRepository.changePassword(any(), any()) } throws Exception()

        val result = changePassword(PASSWORD, PASSWORD)

        Assert.assertEquals(false, result.isSuccess)
    }
}
