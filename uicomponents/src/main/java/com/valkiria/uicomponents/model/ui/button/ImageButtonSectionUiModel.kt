package com.valkiria.uicomponents.model.ui.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier

data class ImageButtonSectionUiModel(
    val identifier: String,
    val options: List<ImageButtonOptionUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
)
