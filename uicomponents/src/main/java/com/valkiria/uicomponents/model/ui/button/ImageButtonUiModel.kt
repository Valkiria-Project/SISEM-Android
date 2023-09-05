package com.valkiria.uicomponents.model.ui.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

class ImageButtonUiModel(
    @DrawableRes val iconResId: Int,
    val label: String?,
    val textStyle: TextStyle?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
)
