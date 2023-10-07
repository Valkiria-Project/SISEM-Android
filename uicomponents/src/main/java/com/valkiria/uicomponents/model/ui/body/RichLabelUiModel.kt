package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class RichLabelUiModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.RICH_LABEL
}
