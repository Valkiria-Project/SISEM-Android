package com.valkiria.uicomponents.components.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

data class TimelineUiModel(
    override val identifier: String,
    val items: List<TimelineItemUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TIMELINE
}
