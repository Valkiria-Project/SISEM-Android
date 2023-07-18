package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.SEGMENTED_SWITCH
import com.valkiria.uicomponents.components.segmentedswitch.OptionUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.props.TextStyle

data class SegmentedSwitchModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val options: List<OptionUiModel>,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = SEGMENTED_SWITCH
}

fun SegmentedSwitchModel.mapToUiModel() = SegmentedSwitchUiModel(
    text = text,
    textStyle = textStyle,
    options = options,
    modifier = modifier
)
