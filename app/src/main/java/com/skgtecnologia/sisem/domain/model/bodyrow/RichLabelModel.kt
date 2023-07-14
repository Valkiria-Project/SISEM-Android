package com.skgtecnologia.sisem.domain.model.bodyrow

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.RICH_LABEL
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.props.TextStyle

data class RichLabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val margins: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = RICH_LABEL
}

fun RichLabelModel.mapToUiModel() = RichLabelUiModel(
    text = text,
    textStyle = textStyle,
    margins = margins
)
