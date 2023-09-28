package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.segmentedswitch.OptionUiModel
import com.valkiria.uicomponents.model.ui.segmentedswitch.SegmentedSwitchUiModel

data class SegmentedSwitchModel(
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

fun SegmentedSwitchModel.mapToUiModel() = SegmentedSwitchUiModel(
    identifier = identifier,
    text = text,
    textStyle = textStyle,
    options = options,
    selected = selected,
    arrangement = arrangement,
    modifier = modifier
)
