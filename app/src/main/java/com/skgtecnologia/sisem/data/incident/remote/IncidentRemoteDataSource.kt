package com.skgtecnologia.sisem.data.incident.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.incident.remote.model.mapToUi
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import javax.inject.Inject

class IncidentRemoteDataSource @Inject constructor(
    private val incidentApi: IncidentApi,
    private val networkApi: NetworkApi
) {

    suspend fun getIncidentInfo(
        idIncident: String,
        idTurn: String,
        codeVehicle: String
    ): Result<IncidentUiModel> = networkApi.apiCall {
        incidentApi.getIncidentInfo(
            idIncident = idIncident,
            idTurn = idTurn,
            codeVehicle = codeVehicle
        )
    }.mapResult {
        it.mapToUi()
    }

    suspend fun getIncidentScreen(codeVehicle: String): Result<ScreenModel> = networkApi.apiCall {
        incidentApi.getIncidentScreen(
            screenBody = ScreenBody(
                params = Params(
                    code = codeVehicle
                )
            )
        )
    }.mapResult {
        it.mapToDomain()
    }
}
