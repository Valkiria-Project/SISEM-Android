package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.core.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.core.data.remote.extensions.createRequestBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.Params
import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import okhttp3.MultipartBody
import javax.inject.Inject

private const val SEND_REPORT_FILE_NAME = "files"

class ReportRemoteDataSource @Inject constructor(
    private val networkApi: NetworkApi,
    private val reportApi: ReportApi
) {

    suspend fun getAddReportRoleScreen(serial: String): Result<ScreenModel> = networkApi.apiCall {
        reportApi.getAddReportRoleScreen(
            screenBody = ScreenBody(params = Params(serial = serial))
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun getAddReportScreen(): Result<ScreenModel> = networkApi.apiCall {
        reportApi.getAddReportEntryScreen(screenBody = ScreenBody(params = Params()))
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun sendReport(
        topic: String,
        description: String,
        images: List<ImageModel>,
        role: OperationRole,
        turnId: String
    ): Result<Unit> = networkApi.apiCall {
        when (role) {
            OperationRole.AUXILIARY_AND_OR_TAPH -> reportApi.sendAuxReport(
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

            OperationRole.DRIVER -> reportApi.sendDriverReport(
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

            OperationRole.MEDIC_APH -> reportApi.sendDoctorReport(
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

            OperationRole.LEAD_APH ->
                throw IllegalArgumentException("Lead APH role not supported")
        }
    }
}
