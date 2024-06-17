package com.skgtecnologia.sisem.data.operation.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.operation.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import javax.inject.Inject

class OperationRemoteDataSource @Inject constructor(
    private val networkApi: NetworkApi,
    private val operationApi: OperationApi
) {

    suspend fun getOperationConfig(serial: String, turnId: String? = null): Result<OperationModel> =
        networkApi.apiCall {
            operationApi.getConfig(serial = serial, idTurn = turnId)
        }.mapResult {
            it.mapToDomain()
        }

    suspend fun logoutTurn(
        idTurn: String,
        idEmployed: String,
        vehicleCode: String
    ): Result<String> = networkApi.apiCall {
        operationApi.logoutTurn(
            idTurn = idTurn,
            idEmployed = idEmployed,
            vehicleCode = vehicleCode
        )
    }.mapResult {
        it.message
    }
}
