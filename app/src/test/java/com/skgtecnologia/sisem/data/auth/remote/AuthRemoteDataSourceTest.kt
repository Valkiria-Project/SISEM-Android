package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.data.auth.remote.model.RefreshTokenResponse
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.domain.auth.model.SessionRefreshException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val REFRESH_TOKEN = "refresh-token"

/**
 * The refresh is the one call where the reason for a failure decides whether a crew stays
 * signed in, so every way it can end is pinned down here.
 */
class AuthRemoteDataSourceTest {

    private val authApi = mockk<AuthApi>()
    private lateinit var dataSource: AuthRemoteDataSource

    @Before
    fun setup() {
        dataSource = AuthRemoteDataSource(authApi, mockk<NetworkApi>(relaxed = true))
    }

    private fun givenRefreshAnswers(response: Response<RefreshTokenResponse>) {
        coEvery { authApi.refresh(any(), any(), any(), any(), any(), any()) } returns response
    }

    private fun givenRefreshThrows(error: Throwable) {
        coEvery { authApi.refresh(any(), any(), any(), any(), any(), any()) } throws error
    }

    private fun keycloakError(code: Int): Response<RefreshTokenResponse> = Response.error(
        code,
        """{"error":"invalid_grant","error_description":"Token is not active"}"""
            .toResponseBody("application/json".toMediaType())
    )

    @Test
    fun `a successful refresh returns the new tokens`() = runTest {
        givenRefreshAnswers(
            Response.success(
                RefreshTokenResponse(
                    accessToken = "new-access",
                    refreshToken = "new-refresh",
                    tokenType = "Bearer",
                    expiresIn = 300
                )
            )
        )

        val result = dataSource.refreshToken(REFRESH_TOKEN)

        assertEquals("new-access", result.getOrThrow().accessToken)
        assertEquals("new-refresh", result.getOrThrow().refreshToken)
    }

    @Test
    fun `a 400 from Keycloak means the session is over`() = runTest {
        givenRefreshAnswers(keycloakError(400))

        val error = dataSource.refreshToken(REFRESH_TOKEN).exceptionOrNull()

        assertTrue(error is SessionRefreshException.Rejected)
        assertEquals(400, (error as SessionRefreshException.Rejected).code)
    }

    @Test
    fun `a 401 from Keycloak means the session is over`() = runTest {
        givenRefreshAnswers(keycloakError(401))

        val error = dataSource.refreshToken(REFRESH_TOKEN).exceptionOrNull()

        assertTrue(error is SessionRefreshException.Rejected)
    }

    @Test
    fun `Keycloak failing on its side is not a verdict on the session`() = runTest {
        givenRefreshAnswers(keycloakError(503))

        val error = dataSource.refreshToken(REFRESH_TOKEN).exceptionOrNull()

        // A Keycloak outage must not sign out every crew on shift at once.
        assertTrue(error is SessionRefreshException.Unreachable)
    }

    @Test
    fun `no signal is not a verdict on the session`() = runTest {
        val noSignal = UnknownHostException("admin.emergencias.saludcapital.gov.co")
        givenRefreshThrows(noSignal)

        val error = dataSource.refreshToken(REFRESH_TOKEN).exceptionOrNull()

        assertTrue(error is SessionRefreshException.Unreachable)
        assertSame(noSignal, error?.cause)
    }

    @Test
    fun `a timeout is not a verdict on the session`() = runTest {
        givenRefreshThrows(SocketTimeoutException("timeout"))

        val error = dataSource.refreshToken(REFRESH_TOKEN).exceptionOrNull()

        assertTrue(error is SessionRefreshException.Unreachable)
    }

    @Test(expected = CancellationException::class)
    fun `cancellation is not swallowed as a failed refresh`() = runTest {
        givenRefreshThrows(CancellationException("scope cancelled"))

        dataSource.refreshToken(REFRESH_TOKEN)
    }
}
