package com.valkiria.uicomponents.components.errorbanner

import androidx.compose.ui.Modifier

data class ErrorUiModel(
    val icon: String? = null,
    val title: String,
    val text: String,
    val modifier: Modifier = Modifier
)
