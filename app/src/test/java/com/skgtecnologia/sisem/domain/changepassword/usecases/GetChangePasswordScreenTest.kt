package com.skgtecnologia.sisem.domain.changepassword.usecases

import com.skgtecnologia.sisem.domain.changepassword.ChangePasswordRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetChangePasswordScreenTest {

    @MockK
    private lateinit var changePasswordRepository: ChangePasswordRepository

    private lateinit var getChangePasswordScreen: GetChangePasswordScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getChangePasswordScreen = GetChangePasswordScreen(changePasswordRepository)
    }

    @Test
    fun `when getChangePasswordScreen is success`() = runTest {
        coEvery { changePasswordRepository.getChangePasswordScreen() } returns mockk()

        val result = getChangePasswordScreen()

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getChangePasswordScreen is failure`() = runTest {
        coEvery { changePasswordRepository.getChangePasswordScreen() } throws Exception()

        val result = getChangePasswordScreen()

        Assert.assertEquals(false, result.isSuccess)
    }
}
