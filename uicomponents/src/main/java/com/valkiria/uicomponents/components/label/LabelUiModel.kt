package com.valkiria.uicomponents.components.label

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

const val DEFAULT_TEXT_COLOR = "#FFFFFF"

data class LabelUiModel(
    override val identifier: String,
    val text: String,
    val overflow: TextOverflow = TextOverflow.Clip,
    val maxLines: Int = Int.MAX_VALUE,
    val textStyle: TextStyle,
    val textColor: String = DEFAULT_TEXT_COLOR,
    val rightIcon: String? = null,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal = Arrangement.Center,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.LABEL
}
