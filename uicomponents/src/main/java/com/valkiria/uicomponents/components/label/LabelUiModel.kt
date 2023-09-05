package com.valkiria.uicomponents.components.label

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

private const val DEFAULT_TEXT_COLOR = "#FFFFFF"

data class LabelUiModel(
    val text: String,
    val textStyle: TextStyle,
    val textColor: String = DEFAULT_TEXT_COLOR,
    val arrangement: Arrangement.Horizontal = Arrangement.Center,
    val modifier: Modifier = Modifier
)
