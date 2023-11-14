package com.skgtecnologia.sisem.domain.report.usecases

import com.skgtecnologia.sisem.domain.report.ReportRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAddReportScreenTest {

    @MockK
    private lateinit var reportRepository: ReportRepository

    private lateinit var getAddReportScreen: GetAddReportScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getAddReportScreen = GetAddReportScreen(reportRepository)
    }

    @Test
    fun `when getAddReportScreen is success`() = runTest {
        coEvery { reportRepository.getAddReportScreen() } returns mockk()

        val result = getAddReportScreen()

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getAddReportScreen is failure`() = runTest {
        coEvery { reportRepository.getAddReportScreen() } throws Exception()

        val result = getAddReportScreen()

        Assert.assertEquals(true, result.isFailure)
    }
}
