package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

private const val USERNAME = "q.conductor"

class GetConflictingActiveSessionTest {

    @MockK
    private lateinit var getAllAccessTokens: GetAllAccessTokens

    private lateinit var getConflictingActiveSession: GetConflictingActiveSession

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getConflictingActiveSession = GetConflictingActiveSession(getAllAccessTokens)
    }

    @Test
    fun `when targetRole is blank it returns null without querying tokens`() = runTest {
        val result = getConflictingActiveSession(USERNAME, targetRole = "")

        Assert.assertNull(result)
    }

    @Test
    fun `when the user has no active session it returns null`() = runTest {
        coEvery { getAllAccessTokens() } returns Result.success(emptyList())

        val result = getConflictingActiveSession(USERNAME, targetRole = "AUXILIARY_AND_OR_TAPH")

        Assert.assertNull(result)
    }

    @Test
    fun `when the active session role matches targetRole it returns null`() = runTest {
        coEvery { getAllAccessTokens() } returns Result.success(
            listOf(accessTokenModel(role = "DRIVER"))
        )

        val result = getConflictingActiveSession(USERNAME, targetRole = "DRIVER")

        Assert.assertNull(result)
    }

    @Test
    fun `when the active session role differs from targetRole it returns the conflicting session`() = runTest {
        val activeSession = accessTokenModel(role = "DRIVER")
        coEvery { getAllAccessTokens() } returns Result.success(listOf(activeSession))

        val result = getConflictingActiveSession(USERNAME, targetRole = "AUXILIARY_AND_OR_TAPH")

        Assert.assertEquals(activeSession, result)
    }

    @Test
    fun `when getAllAccessTokens fails it returns null`() = runTest {
        coEvery { getAllAccessTokens() } returns Result.failure(Throwable("boom"))

        val result = getConflictingActiveSession(USERNAME, targetRole = "AUXILIARY_AND_OR_TAPH")

        Assert.assertNull(result)
    }

    private fun accessTokenModel(role: String) = AccessTokenModel(
        userId = 1,
        dateTime = LocalDateTime.now(),
        accessToken = "access",
        refreshToken = "refresh",
        tokenType = "Bearer",
        username = USERNAME,
        role = role,
        isAdmin = false,
        nameUser = "Q Conductor",
        preoperational = null,
        turn = null,
        isWarning = false,
        docType = "CC",
        document = "123",
        refreshDateTime = LocalDateTime.now(),
        expDate = LocalDateTime.now().plusDays(1)
    )
}
