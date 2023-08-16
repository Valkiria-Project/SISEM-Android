package com.valkiria.uicomponents.components.finding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel

data class FindingUiModel(
    val option: SegmentedSwitchUiModel,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)
