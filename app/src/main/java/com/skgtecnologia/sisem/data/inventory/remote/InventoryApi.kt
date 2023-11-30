package com.skgtecnologia.sisem.data.inventory.remote

import com.skgtecnologia.sisem.data.inventory.remote.model.TransferReturnsBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface InventoryApi {

    @POST("screen/initial-inventory")
    suspend fun getInventoryInitialScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/inventory-biomedical")
    suspend fun getInventoryBiomedicalScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/inventory-medicines")
    suspend fun getInventoryMedicineScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/inventory-vehicle")
    suspend fun getInventoryVehicleScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/transfers-returns")
    suspend fun getTransfersReturnsScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("transfer-return")
    suspend fun saveTransferReturn(@Body transferReturnsBody: TransferReturnsBody): Response<Unit>
}
