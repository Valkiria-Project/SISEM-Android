package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.model.props.TextStyle

data class RichLabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.RICH_LABEL
}

fun RichLabelModel.mapToUiModel() = RichLabelUiModel(
    identifier = identifier,
    text = text,
    textStyle = textStyle,
    arrangement = arrangement,
    modifier = modifier
)
