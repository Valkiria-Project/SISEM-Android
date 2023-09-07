package com.skgtecnologia.sisem.domain.preoperational

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty

interface PreOperationalRepository {

    suspend fun getPreOperationalScreen(androidId: String): ScreenModel

    suspend fun getRole(): OperationRole

    suspend fun sendPreOperational(
        findings: Map<String, Boolean>,
        inventoryValues: Map<String, Int>,
        fieldsValues: Map<String, String>,
        novelties: List<Novelty>
    )
}
