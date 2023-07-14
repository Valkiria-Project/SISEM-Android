package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier

data class TextFieldUiModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardOptions: KeyboardOptions,
    val validations: List<ValidationUiModel>,
    val margins: Modifier = Modifier
)
