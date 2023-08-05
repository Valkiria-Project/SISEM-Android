package com.skgtecnologia.sisem.data.authcards.remote.model

import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConfigResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "preoperational_time") val preoperationalTime: Long,
    @Json(name = "clinic_hist_observations_time") val clinicHistObservationsTime: Long,
    @Json(name = "login_time") val loginTime: Long,
    @Json(name = "num_img_preop_driver") val numImgPreoperationalDriver: Int,
    @Json(name = "num_img_preop_doctor") val numImgPreoperationalDoctor: Int,
    @Json(name = "num_img_preop_aux") val numImgPreoperationalAux: Int,
    @Json(name = "num_img_novelty") val numImgNovelty: Int,
    @Json(name = "auth_method") val authMethod: String,
    @Json(name = "attentions_type") val attentionsType: String,
    @Json(name = "status") val status: Boolean
)

fun ConfigResponse.mapToDomain(): ConfigModel = ConfigModel(
    id = id,
    preoperationalTime = preoperationalTime,
    clinicHistObservationsTime = clinicHistObservationsTime,
    loginTime = loginTime,
    numImgPreoperationalDriver = numImgPreoperationalDriver,
    numImgPreoperationalDoctor = numImgPreoperationalDoctor,
    numImgPreoperationalAux = numImgPreoperationalAux,
    numImgNovelty = numImgNovelty,
    authMethod = authMethod,
    attentionsType = attentionsType,
    status = status
)
