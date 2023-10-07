package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.segmentedswitch.OptionUiModel

data class SegmentedSwitchUiModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val options: List<OptionUiModel>,
    val selected: Boolean = true,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.SEGMENTED_SWITCH
}
