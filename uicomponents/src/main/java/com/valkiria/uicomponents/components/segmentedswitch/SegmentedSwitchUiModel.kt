package com.valkiria.uicomponents.components.segmentedswitch

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class SegmentedSwitchUiModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val options: List<OptionUiModel>,
    val modifier: Modifier = Modifier
)
