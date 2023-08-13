package com.skgtecnologia.sisem.domain.operation

import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel

interface OperationRepository {

    suspend fun getConfig(): ConfigModel

    suspend fun retrieveConfig(): ConfigModel?

    suspend fun storeAmbulanceCode(code: String)
}
