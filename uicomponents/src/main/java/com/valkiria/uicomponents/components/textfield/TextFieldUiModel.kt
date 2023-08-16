package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class TextFieldUiModel(
    val icon: String? = null,
    val placeholder: String? = null,
    val label: String? = null,
    val keyboardOptions: KeyboardOptions,
    val textStyle: TextStyle,
    val validations: List<ValidationUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)
