package com.skgtecnologia.sisem.domain.preoperational

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface PreOperationalRepository {

    suspend fun getPreOperationalScreen(): ScreenModel

    suspend fun sendPreOperational(
        findings: Map<String, Boolean>,
        inventoryValues: Map<String, Int>,
        fieldsValues: Map<String, String>
    )
}
