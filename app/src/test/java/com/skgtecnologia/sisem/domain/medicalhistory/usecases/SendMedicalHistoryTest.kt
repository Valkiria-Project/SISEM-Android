package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SendMedicalHistoryTest {

    @MockK
    private lateinit var medicalHistoryRepository: MedicalHistoryRepository

    private lateinit var sendMedicalHistory: SendMedicalHistory

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sendMedicalHistory = SendMedicalHistory(medicalHistoryRepository)
    }

    @Test
    fun `when sendMedicalHistory is success`() = runTest {
        coEvery {
            medicalHistoryRepository.sendMedicalHistory(
                ID_APH.toString(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Unit

        val result = sendMedicalHistory(
            idAph = ID_APH.toString(),
            humanBodyValues = listOf(),
            segmentedValues = mapOf(),
            signatureValues = mapOf(),
            fieldsValue = mapOf(),
            sliderValues = mapOf(),
            dropDownValues = mapOf(),
            chipSelectionValues = mapOf(),
            chipOptionsValues = mapOf(),
            imageButtonSectionValues = mapOf(),
            vitalSigns = mapOf(),
            infoCardButtonValues = listOf(),
            images = listOf()
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when sendMedicalHistory is failure`() = runTest {
        coEvery {
            medicalHistoryRepository.sendMedicalHistory(
                ID_APH.toString(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Exception()

        val result = sendMedicalHistory(
            idAph = ID_APH.toString(),
            humanBodyValues = listOf(),
            segmentedValues = mapOf(),
            signatureValues = mapOf(),
            fieldsValue = mapOf(),
            sliderValues = mapOf(),
            dropDownValues = mapOf(),
            chipSelectionValues = mapOf(),
            chipOptionsValues = mapOf(),
            imageButtonSectionValues = mapOf(),
            vitalSigns = mapOf(),
            infoCardButtonValues = listOf(),
            images = listOf()
        )

        Assert.assertEquals(true, result.isFailure)
    }
}
