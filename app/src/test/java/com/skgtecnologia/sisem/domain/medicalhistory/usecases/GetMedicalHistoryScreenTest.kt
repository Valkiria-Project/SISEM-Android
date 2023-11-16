package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import com.skgtecnologia.sisem.commons.INCIDENT_CODE
import com.skgtecnologia.sisem.commons.PATIENT_ID
import com.skgtecnologia.sisem.commons.SERIAL
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
                any(),
                any(),
                any()
            )
        } returns mockk()

        val result = getMedicalHistoryScreen(
            serial = SERIAL,
            incidentCode = INCIDENT_CODE,
            patientId = PATIENT_ID
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getMedicalHistoryScreen is failure`() = runTest {
        coEvery {
            medicalHistoryRepository.getMedicalHistoryScreen(
                any(),
                any(),
                any()
            )
        } throws Exception()

        val result = getMedicalHistoryScreen(
            serial = SERIAL,
            incidentCode = INCIDENT_CODE,
            patientId = PATIENT_ID
        )

        Assert.assertEquals(true, result.isFailure)
    }
}
