package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.CHIP
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle

data class ChipModel(
    val identifier: String,
    val icon: String?,
    val text: String,
    val textStyle: TextStyle,
    val style: ChipStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = CHIP
}

fun ChipModel.mapToUiModel() = ChipUiModel(
    icon = icon,
    text = text,
    textStyle = textStyle,
    style = style,
    modifier = modifier
)