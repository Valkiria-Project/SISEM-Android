package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.text.KeyboardOptions
import com.valkiria.uicomponents.props.MarginsUiModel

data class PasswordTextFieldUiModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyboardOptions,
    val validations: List<ValidationUiModel>,
    val margins: MarginsUiModel?
)
