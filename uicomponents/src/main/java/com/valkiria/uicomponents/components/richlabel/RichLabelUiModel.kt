package com.valkiria.uicomponents.components.richlabel

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class RichLabelUiModel(
    val text: String,
    val textStyle: TextStyle,
    val margins: Modifier = Modifier
)
