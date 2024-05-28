package com.skgtecnologia.sisem.data.location.remote.model

import com.squareup.moshi.Json

data class LocationBody(
    @Json(name = "id_vehicle") val idVehicle: String,
    @Json(name = "id_device") val idDevice: String,
    @Json(name = "id_origin") val idOrigin: Int,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "origin_at") val originAt: String,
    @Json(name = "id_incident") val idIncident: String? = null,
)
