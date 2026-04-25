package com.skgtecnologia.sisem.domain.report.usecases

import com.skgtecnologia.sisem.commons.DESCRIPTION
import com.skgtecnologia.sisem.commons.DRIVER_ROLE
import com.skgtecnologia.sisem.commons.TOPIC
import com.skgtecnologia.sisem.domain.report.ReportRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SendReportTest {

    @MockK
    private lateinit var reportRepository: ReportRepository

    private lateinit var sendReport: SendReport

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sendReport = SendReport(reportRepository)
    }

    @Test
    fun `when sendReport is success`() = runTest {
        coEvery { reportRepository.sendReport(any(), any(), any(), any()) } returns Unit

        val result = sendReport(DRIVER_ROLE, TOPIC, DESCRIPTION, emptyList())

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when sendReport is failure`() = runTest {
        coEvery { reportRepository.sendReport(any(), any(), any(), any()) } throws Exception()

        val result = sendReport(DRIVER_ROLE, TOPIC, DESCRIPTION, emptyList())

        Assert.assertEquals(true, result.isFailure)
    }
}
