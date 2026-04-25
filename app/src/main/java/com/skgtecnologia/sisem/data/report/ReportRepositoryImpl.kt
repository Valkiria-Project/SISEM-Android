package com.skgtecnologia.sisem.data.report

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.report.remote.ReportRemoteDataSource
import com.skgtecnologia.sisem.di.operation.OperationRole
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
        roleName: String?,
        topic: String,
        description: String,
        images: List<ImageModel>
    ) {
        val accessToken = if (roleName != null) {
            checkNotNull(authCacheDataSource.retrieveAccessTokenByRole(roleName.lowercase()))
        } else {
            checkNotNull(authCacheDataSource.observeAccessToken().first())
        }
        val role = checkNotNull(OperationRole.getRoleByName(accessToken.role))

        return reportRemoteDataSource.sendReport(
            topic = topic,
            description = description,
            images = images,
            role = role,
            turnId = authCacheDataSource.observeAccessToken().first()?.turn?.id?.toString()
                .orEmpty()
        ).getOrThrow()
    }
}
