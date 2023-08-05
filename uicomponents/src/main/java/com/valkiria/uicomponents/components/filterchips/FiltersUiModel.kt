package com.valkiria.uicomponents.components.filterchips

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class FiltersUiModel(
    val options: List<String>,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)
