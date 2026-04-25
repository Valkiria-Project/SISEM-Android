package com.skgtecnologia.sisem.domain.report.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.report.ReportRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAddReportRoleScreenTest {

    @MockK
    private lateinit var reportRepository: ReportRepository

    private lateinit var getAddReportRoleScreen: GetAddReportRoleScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getAddReportRoleScreen = GetAddReportRoleScreen(reportRepository)
    }

    @Test
    fun `when getAddReportRoleScreen is success`() = runTest {
        coEvery { reportRepository.getAddReportRoleScreen(any()) } returns mockk()

        val result = getAddReportRoleScreen(SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getAddReportRoleScreen is failure`() = runTest {
        coEvery { reportRepository.getAddReportRoleScreen(any()) } throws Exception()

        val result = getAddReportRoleScreen(SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }
}
