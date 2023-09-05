package com.valkiria.uicomponents.model.ui.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.ChipStyle
import com.valkiria.uicomponents.model.props.TextStyle

data class ChipUiModel(
    val icon: String?,
    val text: String,
    val textStyle: TextStyle,
    val style: ChipStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)