package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
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
    fun `when token expired and refresh fails with BannerModel, deletes token and publishes event`() = runTest {
        val bannerError = BannerModel(
            icon = "ic_alert",
            title = "Token expired",
            description = "Session is no longer valid"
        )
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws bannerError
        coEvery { authRepository.deleteAccessTokenByUsername("testuser") } just runs
        coEvery { authRepository.getLastToken() } returns expiredToken.accessToken
        every { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") } just runs

        interceptor.intercept(chain)

        // Verify the logged content contains the BannerModel title, not null
        verify {
            storageProvider.storeContent(
                any(),
                any(),
                match { bytes ->
                    val content = String(bytes)
                    content.contains("Token expired") && content.contains("Session is no longer valid")
                }
            )
        }
        coVerify(exactly = 1) { authRepository.deleteAccessTokenByUsername("testuser") }
        verify(exactly = 1) { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") }
    }

    @Test
    fun `when token expired and refresh fails with generic exception, deletes token and publishes event`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws RuntimeException("network error")
        coEvery { authRepository.deleteAccessTokenByUsername("testuser") } just runs
        coEvery { authRepository.getLastToken() } returns expiredToken.accessToken
        every { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") } just runs

        interceptor.intercept(chain)

        coVerify(exactly = 1) { authRepository.deleteAccessTokenByUsername("testuser") }
        verify(exactly = 1) { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") }
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
