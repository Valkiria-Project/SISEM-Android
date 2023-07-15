package com.skgtecnologia.sisem.domain.model.bodyrow

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.RICH_LABEL
import com.valkiria.uicomponents.components.switch.LabeledSwitchUiModel
import com.valkiria.uicomponents.props.TextStyle

data class LabeledSwitchModel(
    val identifier: String,
    val text: String,
    val textStylessss: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = RICH_LABEL
}

fun LabeledSwitchModel.mapToUiModel() = LabeledSwitchUiModel(
    text = text,
    textStylessss = textStylessss,
    modifier = modifier
)
