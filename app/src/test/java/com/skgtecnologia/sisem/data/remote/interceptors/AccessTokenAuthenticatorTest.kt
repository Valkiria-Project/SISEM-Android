package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class AccessTokenAuthenticatorTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var storageProvider: StorageProvider

    private lateinit var authenticator: AccessTokenAuthenticator

    private val baseRequest = Request.Builder()
        .url("https://example.com/api/data")
        .build()

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

    /**
     * Builds a 401 Response simulating [totalAttemptCount] total attempts.
     *
     * responseCount() counts 1 for the current response plus 1 per priorResponse link.
     * build401Response(1) → responseCount=1, build401Response(4) → responseCount=4.
     * MAX_ATTEMPTS=3: depth ≤ 3 proceeds, depth 4 stops.
     */
    private fun build401Response(totalAttemptCount: Int = 1): Response {
        var response = Response.Builder()
            .request(baseRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .build()

        repeat(totalAttemptCount - 1) {
            response = Response.Builder()
                .request(baseRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
                .priorResponse(response)
                .build()
        }
        return response
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        authenticator = AccessTokenAuthenticator(authRepository, storageProvider)

        every { storageProvider.storeContent(any(), any(), any()) } just runs
        every { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) } just runs
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    @Test
    fun `when 401 on first attempt and refresh succeeds, returns request signed with new token`() = runTest {
        val response = build401Response(totalAttemptCount = 1)
        coEvery { authRepository.observeCurrentAccessToken() } returns flowOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } returns freshToken

        val result = authenticator.authenticate(null, response)

        assertNotNull(result)
        assert(result!!.header("Authorization") == "Bearer new-access-token")
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `when 401 and refresh fails, publishes unauthorized event and returns null without deleting token`() = runTest {
        val response = build401Response(totalAttemptCount = 1)
        coEvery { authRepository.observeCurrentAccessToken() } returns flowOf(expiredToken)
        coEvery { authRepository.refreshToken(expiredToken) } throws RuntimeException("refresh failed")

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 1) { UnauthorizedEventHandler.publishUnauthorizedEvent("testuser") }
    }

    @Test
    fun `when 401 and responseCount exceeds MAX_ATTEMPTS, returns null without refresh`() = runTest {
        // depth=4 → responseCount=4 > MAX_ATTEMPTS=3 → guard fails → return null
        val response = build401Response(totalAttemptCount = 4)

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `when response is not 401, returns null without refresh`() = runTest {
        val response = Response.Builder()
            .request(baseRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(403)
            .message("Forbidden")
            .build()

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
    }

    @Test
    fun `when 401 and no stored token, returns null without refresh or event`() = runTest {
        val response = build401Response(totalAttemptCount = 1)
        coEvery { authRepository.observeCurrentAccessToken() } returns flowOf(null)

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify(exactly = 0) { authRepository.refreshToken(any()) }
        coVerify(exactly = 0) { authRepository.deleteAccessTokenByUsername(any()) }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
