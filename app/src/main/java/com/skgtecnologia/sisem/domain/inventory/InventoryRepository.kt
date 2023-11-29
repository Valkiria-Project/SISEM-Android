package com.skgtecnologia.sisem.domain.inventory

import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface InventoryRepository {

    suspend fun getInventoryInitialScreen(serial: String): ScreenModel

    suspend fun getInventoryViewScreen(inventoryType: InventoryType): ScreenModel

    suspend fun saveTransferReturn(fieldsValues: Map<String, String>)
}
