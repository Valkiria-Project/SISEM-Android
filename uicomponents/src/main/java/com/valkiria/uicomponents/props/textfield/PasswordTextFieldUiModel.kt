package com.valkiria.uicomponents.props.textfield

import com.valkiria.uicomponents.props.KeyBoardUiType
import com.valkiria.uicomponents.props.MarginsUiModel

data class PasswordTextFieldUiModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyBoardUiType,
    val validations: List<ValidationUiModel>,
    val margins: MarginsUiModel?
)
