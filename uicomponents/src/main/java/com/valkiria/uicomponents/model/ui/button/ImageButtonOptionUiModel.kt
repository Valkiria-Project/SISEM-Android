package com.valkiria.uicomponents.model.ui.button

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class ImageButtonOptionUiModel(
    val identifier: String,
    val title: String,
    val textStyle: TextStyle,
    val options: List<ImageButtonUiModel2>,
    val modifier: Modifier
)
