package com.valkiria.uicomponents.model.ui.chip

import com.valkiria.uicomponents.model.props.TextStyle

data class ChipSectionUiModel(
    val title: String,
    val titleTextStyle: TextStyle,
    val listText: List<String>,
    val listTextStyle: TextStyle
)