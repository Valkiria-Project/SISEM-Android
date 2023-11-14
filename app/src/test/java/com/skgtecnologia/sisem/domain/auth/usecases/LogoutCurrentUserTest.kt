package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.domain.auth.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LogoutCurrentUserTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        logoutCurrentUser = LogoutCurrentUser(authRepository)
    }

    @Test
    fun `when logoutCurrentUser is success`() = runTest {
        coEvery { authRepository.logoutCurrentUser() } returns ""

        val result = logoutCurrentUser()

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when logoutCurrentUser is failure`() = runTest {
        coEvery { authRepository.logoutCurrentUser() } throws Exception()

        val result = logoutCurrentUser()

        Assert.assertEquals(false, result.isSuccess)
    }
}
