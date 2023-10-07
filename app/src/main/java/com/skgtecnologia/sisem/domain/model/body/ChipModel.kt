package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.chip.ChipUiModel
import com.valkiria.uicomponents.model.props.ChipStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.body.BodyRowType

data class ChipModel(
    val identifier: String,
    val icon: String?,
    val text: String,
    val textStyle: TextStyle,
    val style: ChipStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP
}

fun ChipModel.mapToUiModel() = ChipUiModel(
    icon = icon,
    text = text,
    textStyle = textStyle,
    style = style,
    arrangement = arrangement,
    modifier = modifier
)
