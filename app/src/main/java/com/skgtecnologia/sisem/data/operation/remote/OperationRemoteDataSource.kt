package com.skgtecnologia.sisem.data.operation.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.operation.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.model.banner.ErrorModelFactory
import javax.inject.Inject

class OperationRemoteDataSource @Inject constructor(
    private val errorModelFactory: ErrorModelFactory,
    private val operationApi: OperationApi
) {

    suspend fun getOperationConfig(serial: String): Result<OperationModel> =
        apiCall(errorModelFactory) {
            operationApi.getConfig(serial = serial)
        }.mapResult {
            it.mapToDomain()
        }
}
