package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetVitalSignsScreenTest {

    @MockK
    private lateinit var medicalHistoryRepository: MedicalHistoryRepository

    private lateinit var getVitalSignsScreen: GetVitalSignsScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getVitalSignsScreen = GetVitalSignsScreen(medicalHistoryRepository)
    }

    @Test
    fun `when getVitalSignsScreen is success`() = runTest {
        coEvery { medicalHistoryRepository.getVitalSignsScreen() } returns mockk()

        val result = getVitalSignsScreen()

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getVitalSignsScreen is failure`() = runTest {
        coEvery { medicalHistoryRepository.getVitalSignsScreen() } throws Exception()

        val result = getVitalSignsScreen()

        Assert.assertEquals(true, result.isFailure)
    }
}
