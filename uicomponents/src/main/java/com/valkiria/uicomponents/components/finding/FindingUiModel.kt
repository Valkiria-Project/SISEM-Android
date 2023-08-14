package com.valkiria.uicomponents.components.finding

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel

data class FindingUiModel(
    val option: SegmentedSwitchUiModel,
    val modifier: Modifier = Modifier
)