package com.skgtecnologia.sisem.domain.operation

import com.skgtecnologia.sisem.domain.authcards.model.OperationModel

interface OperationRepository {

    suspend fun getOperationConfig(serial: String): OperationModel

    suspend fun retrieveOperationConfig(): OperationModel?
}
