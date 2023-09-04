package com.valkiria.uicomponents.components.errorbanner

const val DEFAULT_ICON_COLOR = "#F55757"

data class ErrorUiModel(
    val icon: String? = null,
    val iconColor: String = DEFAULT_ICON_COLOR,
    val title: String,
    val description: String
)
