package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class PreOperationalRemoteDataSource @Inject constructor(
    private val preOperationalApi: PreOperationalApi
) {

    suspend fun getPreOperationalScreen(): Result<ScreenModel> = apiCall {
        preOperationalApi.getPreOperationalScreen(
            screenBody = ScreenBody(params = Params(serial = "serial")) // FIXME: Hardcoded data
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun savePreOperational(): Result<Unit> = apiCall {
        preOperationalApi.savePreOperational(
            screenBody = ScreenBody(params = Params(serial = "serial")) // FIXME: Hardcoded data
        )
    }
}
