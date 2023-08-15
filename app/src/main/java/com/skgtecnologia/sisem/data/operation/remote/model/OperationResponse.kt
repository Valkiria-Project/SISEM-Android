package com.skgtecnologia.sisem.data.operation.remote.model

import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OperationResponse(
    @Json(name = "preoperational_time") val preoperationalTime: Long?,
    @Json(name = "clinic_hist_observations_time") val clinicHistObservationsTime: Long?,
    @Json(name = "login_time") val loginTime: Long?,
    @Json(name = "num_img_preop_driver") val numImgPreoperationalDriver: Int?,
    @Json(name = "num_img_preop_doctor") val numImgPreoperationalDoctor: Int?,
    @Json(name = "num_img_preop_aux") val numImgPreoperationalAux: Int?,
    @Json(name = "num_img_novelty") val numImgNovelty: Int?,
    @Json(name = "auth_method") val authMethod: String?,
    @Json(name = "attentions_type") val attentionsType: String?,
    @Json(name = "status") val status: Boolean?,
    @Json(name = "vehicle_code") val vehicleCode: String?
)

fun OperationResponse.mapToDomain(): ConfigModel = ConfigModel(
    preoperationalTime = preoperationalTime ?: error("Config preoperationalTime cannot be null"),
    clinicHistObservationsTime = clinicHistObservationsTime
        ?: error("Config clinicHistObservationsTime cannot be null"),
    loginTime = loginTime ?: error("Config loginTime cannot be null"),
    numImgPreoperationalDriver = numImgPreoperationalDriver
        ?: error("Config numImgPreoperationalDriver cannot be null"),
    numImgPreoperationalDoctor = numImgPreoperationalDoctor
        ?: error("Config numImgPreoperationalDoctor cannot be null"),
    numImgPreoperationalAux = numImgPreoperationalAux
        ?: error("Config numImgPreoperationalAux cannot be null"),
    numImgNovelty = numImgNovelty ?: error("Config numImgNovelty cannot be null"),
    authMethod = authMethod ?: error("Config authMethod cannot be null"),
    attentionsType = attentionsType ?: error("Config attentionsType cannot be null"),
    status = status ?: error("Config status cannot be null"),
    ambulanceCode = vehicleCode ?: error("Config ambulanceCode cannot be null")
)
