package com.skgtecnologia.sisem.data.inventory.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.inventory.remote.model.TransferReturnsBody
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class InventoryRemoteDataSource @Inject constructor(
    private val inventoryApi: InventoryApi
) {

    suspend fun getInventoryInitialScreen(
        code: String,
        serial: String,
        idTurn: String
    ): Result<ScreenModel> = apiCall {
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
        code: String,
        inventoryType: InventoryType
    ): Result<ScreenModel> = apiCall {
        val screenBody = ScreenBody(
            params = Params(
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

            InventoryType.TRANSFER_AND_RETURN -> inventoryApi.getTransfersReturnsScreen(
                screenBody = screenBody
            )
        }
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun saveTransferReturns(
        idTurn: String,
        fieldsValues: Map<String, String>
    ): Result<Unit> = apiCall {
        inventoryApi.saveTransferReturn(
            transferReturnsBody = TransferReturnsBody(
                idTurn = idTurn,
                fieldsValues = fieldsValues
            )
        )
    }
}
