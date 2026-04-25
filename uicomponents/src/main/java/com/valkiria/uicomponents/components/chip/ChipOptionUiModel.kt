package com.valkiria.uicomponents.components.chip

data class ChipOptionUiModel(
    val id: String,
    val name: String,
    val selected: Boolean,
    val viewsVisibility: Map<String, Boolean>? = null
)
