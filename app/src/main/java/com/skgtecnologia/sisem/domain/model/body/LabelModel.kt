package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.RICH_LABEL
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.props.TextStyle

data class LabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = RICH_LABEL
}

fun LabelModel.mapToUiModel() = LabelUiModel(
    text = text,
    textStyle = textStyle,
    modifier = modifier
)
