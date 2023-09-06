package com.skgtecnologia.sisem.data.report

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.report.remote.ReportRemoteDataSource
import com.skgtecnologia.sisem.domain.report.ReportRepository
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val remoteDataSource: ReportRemoteDataSource
) : ReportRepository {

    override suspend fun sendReport(
        topic: String,
        description: String,
        images: List<ImageModel>
    ) = remoteDataSource.sendReport(
        topic = topic,
        description = description,
        images = images,
        turnId = authCacheDataSource.retrieveAccessToken()?.turn?.id?.toString().orEmpty()
    ).getOrThrow()
}
