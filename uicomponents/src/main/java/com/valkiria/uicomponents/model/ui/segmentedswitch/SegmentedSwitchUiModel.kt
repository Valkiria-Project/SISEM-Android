package com.valkiria.uicomponents.model.ui.segmentedswitch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class SegmentedSwitchUiModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val options: List<OptionUiModel>,
    val selected: Boolean = true,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)
