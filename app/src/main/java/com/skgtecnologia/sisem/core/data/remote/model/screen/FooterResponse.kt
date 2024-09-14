package com.skgtecnologia.sisem.core.data.remote.model.screen

import com.skgtecnologia.sisem.core.data.remote.model.components.button.ButtonResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.footer.FooterUiModel

@JsonClass(generateAdapter = true)
data class FooterResponse(
    @Json(name = "left_button") val leftButton: ButtonResponse?,
    @Json(name = "right_button") val rightButton: ButtonResponse?
)

fun FooterResponse.mapToUi(): FooterUiModel = FooterUiModel(
    leftButton = leftButton?.mapToUi() ?: error("Footer leftButton cannot be null"),
    rightButton = rightButton?.mapToUi()
)
