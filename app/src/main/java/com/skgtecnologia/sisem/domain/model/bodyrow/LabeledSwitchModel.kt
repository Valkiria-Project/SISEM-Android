package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.RICH_LABEL
import com.valkiria.uicomponents.components.switch.LabeledSwitchUiModel
import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyle

data class LabeledSwitchModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val margins: MarginsUiModel?
) : BodyRowModel {

    override val type: BodyRowType = RICH_LABEL
}

fun LabeledSwitchModel.mapToUiModel() = LabeledSwitchUiModel(
    text = text,
    textStyle = textStyle,
    margins = margins
)
