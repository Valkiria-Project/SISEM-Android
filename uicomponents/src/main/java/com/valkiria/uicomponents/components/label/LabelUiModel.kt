package com.valkiria.uicomponents.components.label

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class LabelUiModel(
    val text: String,
    val textStyle: TextStyle,
    val arrangement: Arrangement.Horizontal = Arrangement.Center,
    val modifier: Modifier = Modifier
)
