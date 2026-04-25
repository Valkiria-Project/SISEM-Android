package com.valkiria.uicomponents.model.ui.chip

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class ChipOptionsUiModel(
    val identifier: String,
    val title: String,
    val textStyle: TextStyle,
    val items: List<ChipOptionUiModel>,
    val modifier: Modifier = Modifier
)
