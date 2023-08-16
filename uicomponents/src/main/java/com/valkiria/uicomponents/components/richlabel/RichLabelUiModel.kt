package com.valkiria.uicomponents.components.richlabel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class RichLabelUiModel(
    val text: String,
    val textStyle: TextStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)
