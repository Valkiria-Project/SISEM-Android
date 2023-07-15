package com.valkiria.uicomponents.components.chip

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle

data class ChipUiModel(
    val icon: String?,
    val text: String?,
    val textStylessss: TextStyle,
    val style: ChipStyle,
    val modifier: Modifier = Modifier
)
