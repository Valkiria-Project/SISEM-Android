package com.skgtecnologia.sisem.domain.auth.usecases

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

class DeleteAccessTokenTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var logout: Logout

    private lateinit var deleteAccessToken: DeleteAccessToken

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        deleteAccessToken = DeleteAccessToken(authRepository, logout)
    }

    @Test
    fun `when getAllAccessTokens and logout are success`() = runTest {
        val accessTokens = mockk<AccessTokenModel> {
            coEvery { username } returns USERNAME
        }
        coEvery { authRepository.getAllAccessTokens() } returns listOf(accessTokens)
        coEvery { logout.invoke(any()) } returns Result.success("")

        val result = deleteAccessToken.invoke()

        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(false, result.isFailure)
    }

    @Test
    fun `when getAllAccessTokens is success and logout is failure`() = runTest {
        val accessTokens = mockk<AccessTokenModel> {
            coEvery { username } returns USERNAME
        }
        coEvery { authRepository.getAllAccessTokens() } returns listOf(accessTokens)
        coEvery { logout.invoke(any()) } returns Result.failure(Exception())

        val result = deleteAccessToken.invoke()

        Assert.assertEquals(false, result.isSuccess)
        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when getAllAccessTokens is failure and logout is success`() = runTest {
        val accessTokens = mockk<AccessTokenModel> {
            coEvery { username } returns USERNAME
        }
        coEvery { authRepository.getAllAccessTokens() } returns listOf(accessTokens)
        coEvery { logout.invoke(any()) } returns Result.success("")

        val result = deleteAccessToken.invoke()

        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(false, result.isFailure)
    }

    @Test
    fun `when getAllAccessTokens and logout are failure`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } throws Exception()
        coEvery { logout.invoke(any()) } returns Result.failure(Exception())

        val result = deleteAccessToken.invoke()

        Assert.assertEquals(false, result.isSuccess)
        Assert.assertEquals(true, result.isFailure)
    }
}
