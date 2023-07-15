package com.skgtecnologia.sisem.domain.model.bodyrow

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.CHIP
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle

data class ChipModel(
    val identifier: String?,
    val icon: String?,
    val text: String?,
    val textStylessss: TextStyle,
    val style: ChipStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = CHIP
}

fun ChipModel.mapToUiModel() = ChipUiModel(
    icon = icon,
    text = text,
    textStylessss = textStylessss,
    style = style,
    modifier = modifier
)
