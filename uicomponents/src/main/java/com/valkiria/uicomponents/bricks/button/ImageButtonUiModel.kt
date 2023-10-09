package com.valkiria.uicomponents.bricks.button

import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.TextStyle

class ImageButtonUiModel(
    val identifier: String,
    @DrawableRes val iconResId: Int,
    val label: String?,
    val textStyle: TextStyle?,
    val alignment: Alignment.Horizontal,
    val modifier: Modifier
)
