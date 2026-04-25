package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.domain.auth.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAllAccessTokensTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    private lateinit var getAllAccessTokens: GetAllAccessTokens

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getAllAccessTokens = GetAllAccessTokens(authRepository)
    }

    @Test
    fun `when getAllAccessTokens is success`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf()

        val result = getAllAccessTokens.invoke()

        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(false, result.isFailure)
    }

    @Test
    fun `when getAllAccessTokens is failure`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } throws Exception()

        val result = getAllAccessTokens.invoke()

        Assert.assertEquals(false, result.isSuccess)
        Assert.assertEquals(true, result.isFailure)
    }
}
