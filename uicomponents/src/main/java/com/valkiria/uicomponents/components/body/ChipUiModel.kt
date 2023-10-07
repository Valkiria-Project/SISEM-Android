package com.valkiria.uicomponents.components.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.model.props.ChipStyle
import com.valkiria.uicomponents.model.props.TextStyle

data class ChipUiModel(
    val identifier: String,
    val icon: String?,
    val text: String,
    val textStyle: TextStyle,
    val style: ChipStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.CHIP
}
