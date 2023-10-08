package com.skgtecnologia.sisem.data.remote.model.footer

import com.skgtecnologia.sisem.data.remote.model.body.ButtonResponse
import com.valkiria.uicomponents.components.footerbody.FooterUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FooterResponse(
    @Json(name = "left_button") val leftButton: ButtonResponse?,
    @Json(name = "right_button") val rightButton: ButtonResponse?
)

fun FooterResponse.mapToDomain(): FooterUiModel = FooterUiModel(
    leftButton = leftButton?.mapToUi() ?: error("Footer leftButton cannot be null"),
    rightButton = rightButton?.mapToUi()
)
