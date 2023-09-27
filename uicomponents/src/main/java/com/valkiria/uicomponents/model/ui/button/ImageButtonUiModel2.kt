package com.valkiria.uicomponents.model.ui.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class ImageButtonUiModel2(
    val identifier: String,
    val title: String?,
    val textStyle: TextStyle?,
    val image: String,
    val selected: Boolean,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
)
