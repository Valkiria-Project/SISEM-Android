package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetMedicineScreenTest {

    @MockK
    private lateinit var medicalHistoryRepository: MedicalHistoryRepository

    private lateinit var getMedicineScreen: GetMedicineScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getMedicineScreen = GetMedicineScreen(medicalHistoryRepository)
    }

    @Test
    fun `when getMedicineScreen is success`() = runTest {
        coEvery { medicalHistoryRepository.getMedicineScreen(ANDROID_ID) } returns mockk()

        val result = getMedicineScreen(ANDROID_ID)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getMedicineScreen is failure`() = runTest {
        coEvery { medicalHistoryRepository.getMedicineScreen(ANDROID_ID) } throws Exception()

        val result = getMedicineScreen(ANDROID_ID)

        Assert.assertEquals(true, result.isFailure)
    }
}
