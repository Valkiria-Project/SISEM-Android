package com.valkiria.uicomponents.components.passwordtextfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.props.TextStyle

data class PasswordTextFieldUiModel(
    val icon: String?,
    val placeholder: String?,
    val label: String?,
    val keyboardOptions: KeyboardOptions,
    val textStyle: TextStyle,
    val validations: List<ValidationUiModel>,
    val modifier: Modifier = Modifier
)
