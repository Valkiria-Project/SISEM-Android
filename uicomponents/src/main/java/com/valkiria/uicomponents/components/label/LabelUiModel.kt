package com.valkiria.uicomponents.components.label

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class LabelUiModel(
    val text: String,
    val textStyle: TextStyle,
    val margins: Modifier = Modifier
)
