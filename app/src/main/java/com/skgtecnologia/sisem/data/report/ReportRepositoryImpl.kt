package com.skgtecnologia.sisem.data.report

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.report.remote.ReportRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.ReportRepository
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val reportRemoteDataSource: ReportRemoteDataSource
) : ReportRepository {

    override suspend fun getAddReportRoleScreen(serial: String): ScreenModel =
        reportRemoteDataSource.getAddReportRoleScreen(serial).getOrThrow()

    override suspend fun getAddReportScreen(): ScreenModel =
        reportRemoteDataSource.getAddReportScreen().getOrThrow()

    override suspend fun sendReport(
        topic: String,
        description: String,
        images: List<ImageModel>
    ) = reportRemoteDataSource.sendReport(
        topic = topic,
        description = description,
        images = images,
        turnId = authCacheDataSource.observeAccessToken().first()?.turn?.id?.toString().orEmpty()
    ).getOrThrow()
}
