package com.skgtecnologia.sisem.data.report

import com.skgtecnologia.sisem.commons.DESCRIPTION
import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.commons.TOPIC
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.report.remote.ReportRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ReportRepositoryImplTest {

    @MockK
    private lateinit var authCacheDataSource: AuthCacheDataSource

    @MockK
    private lateinit var reportRemoteDataSource: ReportRemoteDataSource

    private lateinit var reportRepositoryImpl: ReportRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        reportRepositoryImpl = ReportRepositoryImpl(authCacheDataSource, reportRemoteDataSource)
    }

    @Test
    fun `when getAddReportRoleScreen is called`() = runTest {
        coEvery { reportRemoteDataSource.getAddReportRoleScreen(any()) } returns Result.success(
            emptyScreenModel
        )

        val result = reportRepositoryImpl.getAddReportRoleScreen(SERIAL)

        Assert.assertEquals(emptyScreenModel, result)
    }

    @Test
    fun `when getAddReportScreen is called`() = runTest {
        coEvery { reportRemoteDataSource.getAddReportScreen() } returns Result.success(
            emptyScreenModel
        )

        val result = reportRepositoryImpl.getAddReportScreen()

        Assert.assertEquals(emptyScreenModel, result)
    }

    @Test
    fun `when sendReport is called`() = runTest {
        coEvery {
            reportRemoteDataSource.sendReport(
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(
            Unit
        )
        coEvery { authCacheDataSource.observeAccessToken() } returns flow { emit(null) }

        reportRepositoryImpl.sendReport(TOPIC, DESCRIPTION, emptyList())
    }
}
