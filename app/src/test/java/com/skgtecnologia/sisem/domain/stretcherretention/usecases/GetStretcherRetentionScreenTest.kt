package com.skgtecnologia.sisem.domain.stretcherretention.usecases

import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetStretcherRetentionScreenTest {

    @MockK
    private lateinit var stretcherRetentionRepository: StretcherRetentionRepository

    private lateinit var getStretcherRetentionScreen: GetStretcherRetentionScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getStretcherRetentionScreen = GetStretcherRetentionScreen(stretcherRetentionRepository)
    }

    @Test
    fun `when getStretcherRetentionScreen is success`() = runTest {
        coEvery {
            stretcherRetentionRepository.getStretcherRetentionScreen(idAph = ID_APH)
        } returns mockk()

        val result = getStretcherRetentionScreen(
            idAph = ID_APH
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getStretcherRetentionScreen is failure`() = runTest {
        coEvery {
            stretcherRetentionRepository.getStretcherRetentionScreen(idAph = ID_APH)
        } throws Exception()

        val result = getStretcherRetentionScreen(
            idAph = ID_APH
        )

        Assert.assertEquals(true, result.isFailure)
    }
}
