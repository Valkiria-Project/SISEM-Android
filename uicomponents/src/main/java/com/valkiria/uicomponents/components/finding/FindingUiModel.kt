package com.valkiria.uicomponents.components.finding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel

data class FindingUiModel(
    val identifier: String,
    val segmentedSwitchUiModel: SegmentedSwitchUiModel,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINDING
}
