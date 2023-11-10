package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.finding.FindingUiModel

fun getPreOperationalOilFindingUiModel(): FindingUiModel {
    return FindingUiModel(
        identifier = "PRE_OP_OIL",
        segmentedSwitchUiModel = getPreOperationalOilSegmentedSwitchUiModel(),
        segmentedValueUiModel = null,
        readOnly = false,
        findingDetail = null,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 52.dp,
            top = 12.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}
