package com.valkiria.uicomponents.components.textfield

import com.valkiria.uicomponents.components.MarginsUiModel

data class TextFieldUiModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyBoardUiType,
    val validations: List<ValidationUiModel>,
    val margins: MarginsUiModel?
)
