package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

data class ImageButtonSectionUiModel(
    override val identifier: String,
    val options: List<ImageButtonOptionUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON_SECTION
}
