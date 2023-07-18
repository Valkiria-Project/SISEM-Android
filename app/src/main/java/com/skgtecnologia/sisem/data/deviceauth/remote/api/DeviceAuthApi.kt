package com.skgtecnologia.sisem.data.deviceauth.remote.api

import com.skgtecnologia.sisem.data.deviceauth.remote.model.AssociateDeviceBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceAuthApi {

    @POST("sisem-api/v1/screen/deviceAuth")
    suspend fun getDeviceAuthScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("sisem-api/v1/device/associate-device")
    suspend fun associateDevice(@Body associateDeviceBody: AssociateDeviceBody): Response<String>
}
