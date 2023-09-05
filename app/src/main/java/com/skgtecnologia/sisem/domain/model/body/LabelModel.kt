package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.model.props.TextStyle

data class LabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val textColor: String,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.LABEL
}

fun LabelModel.mapToUiModel() = LabelUiModel(
    text = text,
    textStyle = textStyle,
    textColor = textColor,
    arrangement = arrangement,
    modifier = modifier
)
