package com.skgtecnologia.sisem.data.remote.model.footer

import com.skgtecnologia.sisem.data.remote.model.body.ButtonResponse
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FooterResponse(
    @Json(name = "left_button") val leftButton: ButtonResponse?,
    @Json(name = "right_button") val rightButton: ButtonResponse?
)

fun FooterResponse.mapToDomain(): FooterModel = FooterModel(
    leftButton = leftButton?.mapToDomain() as? ButtonModel ?: error("Footer leftButton cannot be null"),
    rightButton = rightButton?.mapToDomain() as? ButtonModel
)
