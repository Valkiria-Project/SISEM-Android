package com.skgtecnologia.sisem.data.news

import com.skgtecnologia.sisem.data.report.remote.ReportRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.news.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource,
) : NewsRepository {

    override suspend fun getAddReportScreen(serial: String): ScreenModel =
        reportRemoteDataSource.getAddReportScreen(serial).getOrThrow()
}
