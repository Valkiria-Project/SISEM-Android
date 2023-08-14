package com.skgtecnologia.sisem.data.remote.model.bricks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.textfield.ValidationUiModel

@JsonClass(generateAdapter = true)
data class ValidationResponse(
    @Json(name = "regex") val regex: String?,
    @Json(name = "message") val message: String?
)

fun ValidationResponse.mapToUi(): ValidationUiModel = ValidationUiModel(
    regex = regex ?: error("Validation regex cannot be null"),
    message = message ?: error("Validation message cannot be null")
)
