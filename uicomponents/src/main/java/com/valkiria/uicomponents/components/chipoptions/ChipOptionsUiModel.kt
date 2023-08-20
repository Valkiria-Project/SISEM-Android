package com.valkiria.uicomponents.components.chipoptions

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class ChipOptionsUiModel(
    val identifier: String,
    val title: String,
    val textStyle: TextStyle,
    val items: List<String>,
    val modifier: Modifier = Modifier
)
