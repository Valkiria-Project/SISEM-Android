package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.extensions.createRequestBody
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import javax.inject.Inject
import okhttp3.MultipartBody

private const val SEND_REPORT_FILE_NAME = "files"

class ReportRemoteDataSource @Inject constructor(
    private val reportApi: ReportApi
) {

    suspend fun getAddReportRoleScreen(serial: String): Result<ScreenModel> = apiCall {
        reportApi.getAddReportRoleScreen(
            screenBody = ScreenBody(params = Params(serial = serial))
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun getAddReportScreen(): Result<ScreenModel> = apiCall {
        reportApi.getAddReportEntryScreen(screenBody = ScreenBody(params = Params()))
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun sendReport(
        topic: String,
        description: String,
        images: List<ImageModel>,
        turnId: String
    ): Result<Unit> = apiCall {
        reportApi.sendReport(
            turnId,
            topic = topic.createRequestBody(),
            description = description.createRequestBody(),
            files = images.map { imageModel ->
                val requestFile = imageModel.file.createRequestBody()
                MultipartBody.Part.createFormData(
                    SEND_REPORT_FILE_NAME,
                    imageModel.file.name,
                    requestFile
                )
            }
        )
    }
}
