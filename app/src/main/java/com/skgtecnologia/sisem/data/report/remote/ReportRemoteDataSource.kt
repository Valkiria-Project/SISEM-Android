package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.data.report.remote.model.buildReportFormDataBody
import com.skgtecnologia.sisem.data.report.remote.model.buildReportImagesBody
import com.skgtecnologia.sisem.domain.model.banner.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import javax.inject.Inject


private const val SEND_REPORT_FILES = "files"

class ReportRemoteDataSource @Inject constructor(
    private val errorModelFactory: ErrorModelFactory,
    private val reportApi: ReportApi
) {

    suspend fun getAddReportRoleScreen(serial: String): Result<ScreenModel> =
        apiCall(errorModelFactory) {
            reportApi.getAddReportRoleScreen(
                screenBody = ScreenBody(params = Params(serial = serial))
            )
        }.mapResult {
            it.mapToDomain()
        }

    suspend fun getAddReportScreen(): Result<ScreenModel> = apiCall(errorModelFactory) {
        reportApi.getAddReportEntryScreen(screenBody = ScreenBody(params = Params()))
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun sendReport(
        topic: String,
        description: String,
        images: List<ImageModel>,
        turnId: String
    ): Result<Unit> = apiCall(errorModelFactory) {
        reportApi.sendReport(
            turnId,
            partMap = buildReportFormDataBody(topic, description),
            files = buildReportImagesBody(images)
        )
    }
}
