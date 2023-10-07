package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier

data class FindingUiModel(
    val identifier: String,
    val segmentedSwitchUiModel: SegmentedSwitchUiModel,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINDING
}
