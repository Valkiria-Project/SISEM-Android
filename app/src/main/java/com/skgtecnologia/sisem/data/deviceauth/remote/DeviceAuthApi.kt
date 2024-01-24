package com.skgtecnologia.sisem.data.deviceauth.remote

import com.skgtecnologia.sisem.data.deviceauth.remote.model.AssociateDeviceBody
import com.skgtecnologia.sisem.data.deviceauth.remote.model.AssociateDeviceResponse
import com.skgtecnologia.sisem.data.deviceauth.remote.model.DeviceResponse
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceAuthApi {

    @POST("screen/deviceAuth")
    suspend fun getDeviceAuthScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("device/associate-device")
    suspend fun associateDevice(
        @Body associateDeviceBody: AssociateDeviceBody
    ): Response<AssociateDeviceResponse>

    @GET("device/code/{code}")
    suspend fun getDeviceType(@Path("code") code: String): Response<DeviceResponse>
}
