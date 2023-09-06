package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.images.mapToBody
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.data.report.remote.model.ReportBody
import com.skgtecnologia.sisem.domain.model.error.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import javax.inject.Inject

class ReportRemoteDataSource @Inject constructor(
    private val errorModelFactory: ErrorModelFactory,
    private val reportApi: ReportApi
) {

    suspend fun getAddReportScreen(serial: String): Result<ScreenModel> =
        apiCall(errorModelFactory) {
            reportApi.getAddReportScreen(screenBody = ScreenBody(params = Params(serial = serial)))
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
            ReportBody(
                topic = topic,
                description = description,
                images = images.map { it.mapToBody() },
                idTurn = turnId.toInt()
            )
        )
    }
}
