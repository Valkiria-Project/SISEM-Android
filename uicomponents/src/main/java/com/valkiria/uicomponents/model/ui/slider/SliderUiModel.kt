package com.valkiria.uicomponents.model.ui.slider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier

data class SliderUiModel(
    val identifier: String,
    val min: Int,
    val max: Int,
    val selected: Int,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
)
