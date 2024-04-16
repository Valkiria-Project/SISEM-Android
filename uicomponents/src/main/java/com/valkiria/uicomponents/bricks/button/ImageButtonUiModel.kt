package com.valkiria.uicomponents.bricks.button

import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment.Horizontal
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle

@Suppress("LongParameterList")
class ImageButtonUiModel(
    val identifier: String,
    @DrawableRes val iconResId: Int,
    val label: String?,
    val textStyle: TextStyle?,
    val size: Dp = 48.dp,
    val alignment: Horizontal,
    val backgroundColor: String? = null,
    val backgroundShape: Shape = RectangleShape,
    val modifier: Modifier = Modifier
)
