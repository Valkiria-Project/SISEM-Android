package com.skgtecnologia.sisem.data.location.remote

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.location.remote.model.LocationBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationApi {

    @POST(BuildConfig.LOCATION_URL.plus("location"))
    suspend fun updateLocation(
        @Body locationBody: LocationBody
    ): Response<Unit>
}
