package com.valkiria.uicomponents.components.finding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedValueUiModel

data class FindingUiModel(
    override val identifier: String,
    val segmentedSwitchUiModel: SegmentedSwitchUiModel?,
    val segmentedValueUiModel: SegmentedValueUiModel?,
    val readOnly: Boolean,
    val findingDetail: FindingsDetailUiModel?,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINDING
}
