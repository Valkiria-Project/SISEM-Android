package com.valkiria.uicomponents.components.crewmembercard

import com.valkiria.uicomponents.props.TextStyle

data class ChipSectionUiModel(
    val title: String,
    val titleTextStyle: TextStyle,
    val listText: List<String>,
    val listTextStyle: TextStyle
)