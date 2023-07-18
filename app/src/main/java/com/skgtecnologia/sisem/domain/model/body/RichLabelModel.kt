package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.RICH_LABEL
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.SEGMENTED_SWITCH
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.props.TextStyle

data class RichLabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = RICH_LABEL
}

fun RichLabelModel.mapToUiModel() = RichLabelUiModel(
    text = text,
    textStyle = textStyle,
    modifier = modifier
)
