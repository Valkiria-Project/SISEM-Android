package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.commons.DESCRIPTION
import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.commons.TOPIC
import com.skgtecnologia.sisem.commons.TURN_ID
import com.skgtecnologia.sisem.commons.footerResponseMock
import com.skgtecnologia.sisem.commons.headerResponseMock
import com.skgtecnologia.sisem.commons.imageButtonResponseMock
import com.skgtecnologia.sisem.commons.imageButtonSectionResponseMock
import com.skgtecnologia.sisem.commons.imageModelMock
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.di.operation.OperationRole
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ReportRemoteDataSourceTest {

    @MockK
    private lateinit var storageProvider: StorageProvider

    @MockK
    private lateinit var stringProvider: StringProvider

    @MockK
    private lateinit var reportApi: ReportApi

    private lateinit var networkApi: NetworkApi

    private lateinit var reportRemoteDataSource: ReportRemoteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { storageProvider.storeContent(any(), any(), any()) } returns Unit
        networkApi = NetworkApi(storageProvider, stringProvider)

        reportRemoteDataSource = ReportRemoteDataSource(networkApi, reportApi)
    }

    @Test
    fun `when getAddReportRoleScreen is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(imageButtonResponseMock),
            footer = footerResponseMock
        )
        coEvery { reportApi.getAddReportRoleScreen(any()) } returns Response.success(screenResponse)

        val result = reportRemoteDataSource.getAddReportRoleScreen(SERIAL)

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getAddReportRoleScreen is failure`() = runTest {
        coEvery { reportApi.getAddReportRoleScreen(any()) } returns Response.error(
            400,
            "".toResponseBody()
        )

        val result = reportRemoteDataSource.getAddReportRoleScreen(SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when getAddReportScreen is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(imageButtonSectionResponseMock),
            footer = footerResponseMock
        )
        coEvery {
            reportApi.getAddReportEntryScreen(any())
        } returns Response.success(screenResponse)

        val result = reportRemoteDataSource.getAddReportScreen()

        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrNull())
    }

    @Test
    fun `when getAddReportScreen is failure`() = runTest {
        coEvery { reportApi.getAddReportEntryScreen(any()) } returns Response.error(
            400,
            "".toResponseBody()
        )

        val result = reportRemoteDataSource.getAddReportScreen()

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when sendReport is success`() = runTest {
        coEvery { reportApi.sendDriverReport(any(), any(), any(), any()) } returns Response.success(
            Unit
        )

        val result = reportRemoteDataSource.sendReport(
            TOPIC, DESCRIPTION, emptyList(), OperationRole.DRIVER, TURN_ID
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when sendReport is failure`() = runTest {
        coEvery { reportApi.sendAuxReport(any(), any(), any(), any()) } returns Response.error(
            400,
            "".toResponseBody()
        )

        val result = reportRemoteDataSource.sendReport(
            TOPIC, DESCRIPTION, listOf(imageModelMock), OperationRole.AUXILIARY_AND_OR_TAPH, TURN_ID
        )

        Assert.assertEquals(true, result.isFailure)
    }
}
