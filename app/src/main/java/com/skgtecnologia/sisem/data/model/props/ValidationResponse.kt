package com.skgtecnologia.sisem.data.model.props

import com.valkiria.uicomponents.components.textfield.ValidationUiModel

data class ValidationResponse(
    val regex: String?,
    val message: String?
)

fun ValidationResponse.mapToUi(): ValidationUiModel = ValidationUiModel(
    regex = regex.orEmpty(),
    message = message.orEmpty()
)
