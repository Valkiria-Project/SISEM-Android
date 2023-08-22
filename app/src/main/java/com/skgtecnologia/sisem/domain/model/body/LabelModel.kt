package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.props.TextStyle

data class LabelModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.LABEL
}

fun LabelModel.mapToUiModel() = LabelUiModel(
    text = text,
    textStyle = textStyle,
    arrangement = arrangement,
    modifier = modifier
)
