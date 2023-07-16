package com.skgtecnologia.sisem.data.remote.model.props

import com.valkiria.uicomponents.components.textfield.ValidationUiModel

data class ValidationResponse(
    val regex: String?,
    val message: String?
)

fun ValidationResponse.mapToUi(): ValidationUiModel = ValidationUiModel(
    regex = regex ?: error("Validation regex cannot be null"),
    message = message ?: error("Validation message cannot be null")
)