package com.valkiria.uicomponents.components.segmentedswitch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextStyle

data class SegmentedSwitchUiModel(
    override val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val options: List<OptionUiModel>,
    val selected: Boolean = true,
    val selectionVisibility: Map<String, List<String>>? = null,
    val deselectionVisibility: Map<String, List<String>>? = null,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.SEGMENTED_SWITCH
}
