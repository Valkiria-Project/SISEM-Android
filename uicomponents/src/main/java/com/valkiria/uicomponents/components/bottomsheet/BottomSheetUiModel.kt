package com.valkiria.uicomponents.components.bottomsheet

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.valkiria.uicomponents.props.TextStyle

data class BottomSheetUiModel(
    val icon: Painter?,
    val title: String,
    val titleTextStyle: TextStyle,
    val subtitle: String? = null,
    val subtitleTextStyle: TextStyle? = null,
    val text: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)
