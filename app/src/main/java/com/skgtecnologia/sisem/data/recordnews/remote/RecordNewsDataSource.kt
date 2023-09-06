package com.skgtecnologia.sisem.data.recordnews.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.data.report.remote.ReportApi
import com.skgtecnologia.sisem.domain.model.error.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class RecordNewsDataSource @Inject constructor(
    private val errorModelFactory: ErrorModelFactory,
    private val reportApi: ReportApi
) {

    suspend fun getRecordNewsScreen(): Result<ScreenModel> = apiCall(errorModelFactory) {
        reportApi.getAddReportEntryScreen(screenBody = ScreenBody(params = Params()))
    }.mapResult {
        it.mapToDomain()
    }
}
