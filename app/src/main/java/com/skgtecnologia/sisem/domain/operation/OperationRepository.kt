package com.skgtecnologia.sisem.domain.operation

import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import kotlinx.coroutines.flow.Flow

interface OperationRepository {

    suspend fun getOperationConfig(serial: String): OperationModel

    suspend fun logoutTurn(username: String): String

    fun observeOperationConfig(): Flow<OperationModel?>
}
