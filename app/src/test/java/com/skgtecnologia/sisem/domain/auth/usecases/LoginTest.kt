package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.commons.PASSWORD
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoginTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    private lateinit var login: Login

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        login = Login(authRepository)
    }

    @Test
    fun `when authenticate is success`() = runTest {
        coEvery { authRepository.authenticate(any(), any()) } returns mockk<AccessTokenModel>()

        val result = login(USERNAME, PASSWORD)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when authenticate is failure`() = runTest {
        coEvery { authRepository.authenticate(any(), any()) } throws Exception()

        val result = login(USERNAME, PASSWORD)

        Assert.assertEquals(false, result.isSuccess)
    }
}
