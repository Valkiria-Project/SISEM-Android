package com.valkiria.uicomponents.model.ui.finding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.segmentedswitch.SegmentedSwitchUiModel

data class FindingUiModel(
    val option: SegmentedSwitchUiModel,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)
