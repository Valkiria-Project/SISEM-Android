package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier

data class PasswordTextFieldUiModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyboardOptions,
    val validations: List<ValidationUiModel>,
    val margins: Modifier = Modifier
)
