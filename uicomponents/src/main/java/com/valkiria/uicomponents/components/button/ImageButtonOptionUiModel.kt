package com.valkiria.uicomponents.components.button

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.TextUiModel

data class ImageButtonOptionUiModel(
    val identifier: String,
    val title: TextUiModel,
    val options: List<ImageButtonUiModel>,
    val modifier: Modifier
)
