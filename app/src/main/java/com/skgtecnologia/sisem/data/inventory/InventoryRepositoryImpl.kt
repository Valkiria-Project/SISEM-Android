package com.skgtecnologia.sisem.data.inventory

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.inventory.remote.InventoryRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.inventory.InventoryRepository
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource,
    private val inventoryRemoteDataSource: InventoryRemoteDataSource
) : InventoryRepository {

    override suspend fun getInventoryInitialScreen(serial: String): ScreenModel {
        val accessToken = checkNotNull(authCacheDataSource.observeAccessToken())

        return inventoryRemoteDataSource.getInventoryInitialScreen(
            serial = serial,
            code = operationCacheDataSource.observeOperationConfig()
                .first()?.vehicleCode.orEmpty(),
            idTurn = accessToken.first()?.turn?.id?.toString().orEmpty()
        ).getOrThrow()
    }

    override suspend fun getInventoryViewScreen(
        inventoryType: InventoryType,
        serial: String
    ): ScreenModel = inventoryRemoteDataSource.getInventoryViewScreen(
        serial = serial,
        code = operationCacheDataSource.observeOperationConfig()
            .first()?.vehicleCode.orEmpty(),
        inventoryType = inventoryType
    ).getOrThrow()

    override suspend fun saveTransferReturn(
        fieldsValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        labelValues: Map<String, String>
    ) = inventoryRemoteDataSource.saveTransferReturns(
        idTurn = authCacheDataSource.observeAccessToken().first()?.turn?.id?.toString()
            .orEmpty(),
        fieldsValues = fieldsValues,
        dropDownValues = dropDownValues,
        chipSelectionValues = chipSelectionValues,
        labelValues = labelValues
    ).getOrThrow()
}
