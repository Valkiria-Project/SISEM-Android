package com.skgtecnologia.sisem.data.operation

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.auth.remote.AuthRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.operation.remote.OperationRemoteDataSource
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val USERNAME = "crew_member"
private const val REFRESH_TOKEN = "refresh_token_value"
private const val TURN_ID = "42"

class OperationRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK private lateinit var authCacheDataSource: AuthCacheDataSource

    @MockK private lateinit var authRemoteDataSource: AuthRemoteDataSource

    @MockK private lateinit var operationCacheDataSource: OperationCacheDataSource

    @MockK private lateinit var operationRemoteDataSource: OperationRemoteDataSource

    private lateinit var repository: OperationRepositoryImpl

    private val accessToken = mockk<AccessTokenModel>(relaxed = true) {
        every { username } returns USERNAME
        every { userId } returns 7
        every { refreshToken } returns REFRESH_TOKEN
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        repository = OperationRepositoryImpl(
            authCacheDataSource,
            authRemoteDataSource,
            operationCacheDataSource,
            operationRemoteDataSource
        )

        coEvery { authCacheDataSource.observeAccessToken() } returns flowOf(accessToken)
        coEvery {
            authCacheDataSource.retrieveAccessTokenByUsername(USERNAME)
        } returns accessToken
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flowOf(null)
        coEvery {
            operationRemoteDataSource.logoutTurn(any(), any(), any())
        } returns Result.success(TURN_ID)
    }

    @Test
    fun `logoutTurn invalidates the session with the crew member refresh token`() = runTest {
        coEvery {
            authRemoteDataSource.logout(username = USERNAME, refreshToken = REFRESH_TOKEN)
        } returns Result.success(USERNAME)

        val result = repository.logoutTurn(USERNAME)

        assertEquals(TURN_ID, result)
        coVerify(exactly = 1) {
            authRemoteDataSource.logout(username = USERNAME, refreshToken = REFRESH_TOKEN)
        }
        coVerify(exactly = 1) {
            authCacheDataSource.deleteAccessTokenByUsername(username = USERNAME)
        }
    }

    @Test
    fun `logoutTurn keeps the cached token when the session logout fails`() = runTest {
        coEvery {
            authRemoteDataSource.logout(username = USERNAME, refreshToken = REFRESH_TOKEN)
        } returns Result.failure(IllegalStateException("logout failed"))

        val result = runCatching { repository.logoutTurn(USERNAME) }

        assertEquals(true, result.isFailure)
        coVerify(exactly = 0) {
            authCacheDataSource.deleteAccessTokenByUsername(username = any())
        }
    }

    @Test
    fun `logoutTurn does not invalidate the session when closing the turn fails`() = runTest {
        coEvery {
            operationRemoteDataSource.logoutTurn(any(), any(), any())
        } returns Result.failure(IllegalStateException("turn close failed"))

        val result = runCatching { repository.logoutTurn(USERNAME) }

        assertEquals(true, result.isFailure)
        coVerify(exactly = 0) { authRemoteDataSource.logout(any(), any()) }
    }
}
