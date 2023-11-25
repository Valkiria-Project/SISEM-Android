package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetMedicalHistoryScreenTest {

    @MockK
    private lateinit var medicalHistoryRepository: MedicalHistoryRepository

    private lateinit var getMedicalHistoryScreen: GetMedicalHistoryScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getMedicalHistoryScreen = GetMedicalHistoryScreen(medicalHistoryRepository)
    }

    @Test
    fun `when getMedicalHistoryScreen is success`() = runTest {
        coEvery {
            medicalHistoryRepository.getMedicalHistoryScreen(
                idAph = ID_APH
            )
        } returns mockk()

        val result = getMedicalHistoryScreen(
            idAph = ID_APH
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getMedicalHistoryScreen is failure`() = runTest {
        coEvery {
            medicalHistoryRepository.getMedicalHistoryScreen(
                idAph = ID_APH
            )
        } throws Exception()

        val result = getMedicalHistoryScreen(
            idAph = ID_APH
        )

        Assert.assertEquals(true, result.isFailure)
    }
}
