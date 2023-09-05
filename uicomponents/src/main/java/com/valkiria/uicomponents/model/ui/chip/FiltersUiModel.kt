package com.valkiria.uicomponents.model.ui.chip

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class FiltersUiModel(
    val options: List<String>,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)
