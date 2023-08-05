package com.valkiria.uicomponents.components.filterchips

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class FilterChipsUiModel(
    val chips: List<String>,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)
