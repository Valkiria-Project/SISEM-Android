package com.skgtecnologia.sisem.data.inventory.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.core.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.core.data.remote.model.screen.Params
import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.data.inventory.remote.model.TransferReturnsBody
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class InventoryRemoteDataSource @Inject constructor(
    private val inventoryApi: InventoryApi,
    private val networkApi: NetworkApi
) {

    suspend fun getInventoryInitialScreen(
        code: String,
        serial: String,
        idTurn: String
    ): Result<ScreenModel> = networkApi.apiCall {
        inventoryApi.getInventoryInitialScreen(
            screenBody = ScreenBody(
                params = Params(
                    code = code,
                    serial = serial,
                    turnId = idTurn
                )
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun getInventoryViewScreen(
        serial: String,
        code: String,
        inventoryType: InventoryType
    ): Result<ScreenModel> = networkApi.apiCall {
        val screenBody = ScreenBody(
            params = Params(
                serial = serial,
                code = code
            )
        )

        when (inventoryType) {
            InventoryType.BIOMEDICAL -> inventoryApi.getInventoryBiomedicalScreen(
                screenBody = screenBody
            )

            InventoryType.MEDICINE -> inventoryApi.getInventoryMedicineScreen(
                screenBody = screenBody
            )

            InventoryType.VEHICLE -> inventoryApi.getInventoryVehicleScreen(
                screenBody = screenBody
            )

            InventoryType.TRANSFER_RETURNS -> inventoryApi.getTransfersReturnsScreen(
                screenBody = screenBody
            )
        }
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun saveTransferReturns(
        idTurn: String,
        fieldsValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        labelValues: Map<String, String>
    ): Result<Unit> = networkApi.apiCall {
        inventoryApi.saveTransferReturn(
            transferReturnsBody = TransferReturnsBody(
                idTurn = idTurn,
                fieldsValues = fieldsValues,
                dropDownValues = dropDownValues,
                chipSelectionValues = chipSelectionValues,
                labelValues = labelValues
            )
        )
    }
}
