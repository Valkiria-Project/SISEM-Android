package com.skgtecnologia.sisem.domain.authcards.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAuthCardsScreenTest {

    @MockK
    private lateinit var authCardsRepository: AuthCardsRepository

    private lateinit var getAuthCardsScreen: GetAuthCardsScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getAuthCardsScreen = GetAuthCardsScreen(authCardsRepository)
    }

    @Test
    fun `when getAuthCardsScreen is success`() = runTest {
        coEvery { authCardsRepository.getAuthCardsScreen(any()) } returns mockk()

        val result = getAuthCardsScreen.invoke(SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getAuthCardsScreen is failure`() = runTest {
        coEvery { authCardsRepository.getAuthCardsScreen(any()) } throws Exception()

        val result = getAuthCardsScreen.invoke(SERIAL)

        Assert.assertEquals(false, result.isSuccess)
    }
}
