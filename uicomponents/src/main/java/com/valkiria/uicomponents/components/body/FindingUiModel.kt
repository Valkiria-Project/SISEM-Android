package com.valkiria.uicomponents.components.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowType

data class FindingUiModel(
    val identifier: String,
    val segmentedSwitchUiModel: com.valkiria.uicomponents.components.body.SegmentedSwitchUiModel,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINDING
}
