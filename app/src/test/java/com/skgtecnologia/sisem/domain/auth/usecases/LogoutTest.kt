package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.domain.auth.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val USERNAME = "username"

class LogoutTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    private lateinit var logout: Logout

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        logout = Logout(authRepository)
    }

    @Test
    fun `when logout is success`() = runTest {
        coEvery { authRepository.logout(USERNAME) } returns ""

        val result = logout(USERNAME)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when logout is failure`() = runTest {
        coEvery { authRepository.logout(USERNAME) } throws Exception()

        val result = logout(USERNAME)

        Assert.assertEquals(false, result.isSuccess)
    }
}
