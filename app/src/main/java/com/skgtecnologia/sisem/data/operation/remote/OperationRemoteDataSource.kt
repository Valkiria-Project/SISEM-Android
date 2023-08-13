package com.skgtecnologia.sisem.data.operation.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.operation.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import javax.inject.Inject

class OperationRemoteDataSource @Inject constructor(
    private val operationApi: OperationApi
) {

    suspend fun getConfig(): Result<ConfigModel> = apiCall {
        operationApi.getConfig()
    }.mapResult {
        it.mapToDomain()
    }
}
