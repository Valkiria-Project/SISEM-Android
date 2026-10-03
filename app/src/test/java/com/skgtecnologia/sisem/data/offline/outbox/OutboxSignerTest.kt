package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.SessionRefreshException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class OutboxSignerTest {

    private val authRepository = mockk<AuthRepository>()
    private val signer = OutboxSigner(authRepository) { NOW }
    private val replay = Request.Builder().url(BASE + "aph").header(OUTBOX_REPLAY_HEADER, "r1").build()

    private fun SigningResult.token() = (this as SigningResult.Signed).request.bearerToken()

    @Test
    fun `a write is signed as the user who made it`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(token("aux2"), token("aux1"))

        assertEquals("token-aux1", signer.sign(replay, "aux1").token())
    }

    @Test
    fun `after a shift change it waits for its creator instead of using the new crew member`() = runTest {
        // aux2 now holds the same role on this device; aux1 made the write and signed out.
        coEvery { authRepository.getAllAccessTokens() } returns listOf(token("aux2"))

        assertTrue(signer.sign(replay, "aux1") is SigningResult.WaitForCreator)
    }

    @Test
    fun `an expired session is refreshed before signing`() = runTest {
        val expired = token("aux1", expDate = NOW.minusMinutes(1))
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expired)
        coEvery { authRepository.refreshToken(expired) } returns token("aux1", accessToken = "fresh")

        assertEquals("fresh", signer.sign(replay, "aux1").token())
        coVerify(exactly = 1) { authRepository.refreshToken(expired) }
    }

    @Test
    fun `a refresh that fails, for any reason, makes it wait`() = runTest {
        val expired = token("aux1", expDate = NOW.minusMinutes(1))
        coEvery { authRepository.getAllAccessTokens() } returns listOf(expired)

        coEvery { authRepository.refreshToken(expired) } throws SessionRefreshException.Rejected(400)
        assertTrue(signer.sign(replay, "aux1") is SigningResult.WaitForCreator)

        coEvery { authRepository.refreshToken(expired) } throws IOException("no signal")
        assertTrue(signer.sign(replay, "aux1") is SigningResult.WaitForCreator)
    }

    @Test
    fun `a write made with nobody signed in has no one to send it as`() = runTest {
        assertEquals(SigningResult.NoCreator, signer.sign(replay, null))
    }
}
