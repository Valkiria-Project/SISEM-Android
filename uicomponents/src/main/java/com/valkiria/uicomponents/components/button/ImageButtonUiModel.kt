package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel

data class ImageButtonUiModel(
    override val identifier: String,
    val title: TextUiModel?,
    val image: String,
    val selected: Boolean,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON
}
