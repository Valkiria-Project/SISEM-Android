package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.LABEL
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.SEGMENTED_SWITCH
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.props.TextStyle

data class LabelModel(
    val text: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = LABEL
}

fun LabelModel.mapToUiModel() = LabelUiModel(
    text = text,
    textStyle = textStyle,
    modifier = modifier
)
