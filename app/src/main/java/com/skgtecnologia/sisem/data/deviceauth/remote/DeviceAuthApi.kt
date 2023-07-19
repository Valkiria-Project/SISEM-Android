package com.skgtecnologia.sisem.data.deviceauth.remote

import com.skgtecnologia.sisem.data.deviceauth.remote.model.AssociateDeviceBody
import com.skgtecnologia.sisem.data.deviceauth.remote.model.AssociateDeviceResponse
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceAuthApi {

    @POST("screen/deviceAuth")
    suspend fun getDeviceAuthScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("device/associate-device")
    suspend fun associateDevice(
        @Body associateDeviceBody: AssociateDeviceBody
    ): Response<AssociateDeviceResponse>
}
