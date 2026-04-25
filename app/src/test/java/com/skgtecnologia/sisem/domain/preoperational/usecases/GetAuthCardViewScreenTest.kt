package com.skgtecnologia.sisem.domain.preoperational.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAuthCardViewScreenTest {

    @MockK
    private lateinit var preOperationalRepository: PreOperationalRepository

    private lateinit var getAuthCardViewScreen: GetAuthCardViewScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getAuthCardViewScreen = GetAuthCardViewScreen(preOperationalRepository)
    }

    @Test
    fun `when getAuthCardViewScreen is success`() = runTest {
        coEvery { preOperationalRepository.getAuthCardViewScreen(any()) } returns mockk()

        val result = getAuthCardViewScreen.invoke(SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getAuthCardViewScreen is failure`() = runTest {
        coEvery { preOperationalRepository.getAuthCardViewScreen(any()) } throws Exception()

        val result = getAuthCardViewScreen.invoke(SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }
}
