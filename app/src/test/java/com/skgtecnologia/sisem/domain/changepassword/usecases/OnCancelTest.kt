package com.skgtecnologia.sisem.domain.changepassword.usecases

import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class OnCancelTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var logout: Logout

    private lateinit var onCancel: OnCancel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        onCancel = OnCancel(authRepository, logout)
    }

    @Test
    fun `when invoke and dependencies are success`() = runTest {
        val accessToken = mockk<AccessTokenModel> {
            coEvery { username } returns USERNAME
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { emit(accessToken) }
        coEvery { logout.invoke(any()) } returns Result.success("")
        coEvery { authRepository.deleteAccessTokenByUsername(any()) } returns Unit

        val result = onCancel.invoke()

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when deleteAccessTokenByUsername is failure`() = runTest {
        val accessToken = mockk<AccessTokenModel> {
            coEvery { username } returns USERNAME
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { emit(accessToken) }
        coEvery { logout.invoke(any()) } returns Result.success("")
        coEvery { authRepository.deleteAccessTokenByUsername(any()) } throws Exception()

        val result = onCancel.invoke()

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when logout is failure`() = runTest {
        val accessToken = mockk<AccessTokenModel> {
            coEvery { username } returns USERNAME
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { emit(accessToken) }
        coEvery { logout.invoke(any()) } returns Result.failure(Exception())

        val result = onCancel.invoke()

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when observeCurrentAccessToken is failure`() = runTest {
        coEvery { authRepository.observeCurrentAccessToken() } throws Exception()

        val result = onCancel.invoke()

        Assert.assertEquals(true, result.isFailure)
    }
}
