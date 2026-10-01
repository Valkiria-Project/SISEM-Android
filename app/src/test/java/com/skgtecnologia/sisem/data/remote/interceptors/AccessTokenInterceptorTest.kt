package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.model.SessionRefreshException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class AccessTokenInterceptorTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var storageProvider: StorageProvider

    @MockK
    private lateinit var chain: Interceptor.Chain

    private lateinit var interceptor: AccessTokenInterceptor

    private val expiredToken = AccessTokenModel(
        userId = 1,
        dateTime = LocalDateTime.now().minusHours(2),
        accessToken = "old-access-token",
        refreshToken = "old-refresh-token",
        tokenType = "Bearer",
        username = "testuser",
        role = "auxiliary_and_or_taph",
        isAdmin = false,
        nameUser = "Test User",
        preoperational = null,
        turn = null,
        isWarning = false,
        docType = "CC",
        document = "12345",
        refreshDateTime = LocalDateTime.now().minusHours(2),
        expDate = LocalDateTime.now().minusMinutes(10)
    )

    private val freshToken = expiredToken.copy(
        accessToken = "new-access-token",
        refreshToken = "new-refresh-token",
        expDate = LocalDateTime.now().plusMinutes(5),
        refreshDateTime = LocalDateTime.now()
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        interceptor = AccessTokenInterceptor(authRepository, storageProvider)

        every { storageProvider.storeContent(any(), any(), any()) } just runs

        val dummyRequest = Request.Builder().url("https://example.com/api/data").build()
        every { chain.request() } returns dummyRequest
        every { chain.proceed(any()) } returns Response.Builder()
            .request(dummyRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    @Test
    fun `when token is expired and refresh succeeds, no token deletion and no unauthorized event`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } returns freshToken
        coEvery { authRepository.getLastToken() } returns freshToken.accessToken

        interceptor.intercept(chain)

        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `when Keycloak rejects the refresh, deletes dead token and publishes event`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws
            SessionRefreshException.Rejected(code = 400)
        coEvery { authRepository.deleteAccessTokenByUsername("testuser") } just runs
        coEvery { authRepository.getLastToken() } returns expiredToken.accessToken
        every { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") } just runs

        interceptor.intercept(chain)

        verify {
            storageProvider.storeContent(
                any(),
                any(),
                match { bytes -> String(bytes).contains("Refresh rejected with HTTP 400") }
            )
        }
        coVerify(exactly = 1) { authRepository.deleteAccessTokenByUsername("testuser") }
        verify(exactly = 1) { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") }
    }

    @Test
    fun `when the refresh cannot reach the server, keeps the token and the session`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws
            SessionRefreshException.Unreachable(detail = "UnknownHostException")
        coEvery { authRepository.getLastToken() } returns expiredToken.accessToken

        interceptor.intercept(chain)

        // An ambulance in a dead zone: the failure is logged, but nobody is signed out.
        verify {
            storageProvider.storeContent(
                any(),
                any(),
                match { bytes -> String(bytes).contains("UnknownHostException") }
            )
        }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `an unexpected failure does not end the session either`() = runTest {
        // This used to assert the opposite: a plain exception signed the user out. Only an
        // explicit rejection should do that.
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws RuntimeException("boom")
        coEvery { authRepository.getLastToken() } returns expiredToken.accessToken

        interceptor.intercept(chain)

        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `when token is not expired, no refresh is attempted`() = runTest {
        val validToken = expiredToken.copy(expDate = LocalDateTime.now().plusMinutes(5))
        coEvery { authRepository.getAllAccessTokens() } returns listOf(validToken)
        coEvery { authRepository.getLastToken() } returns validToken.accessToken

        interceptor.intercept(chain)

        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
    }

    @Test
    fun `when token list is empty, no refresh and no event`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns emptyList()
        coEvery { authRepository.getLastToken() } returns null

        interceptor.intercept(chain)

        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
