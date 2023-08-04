package com.skgtecnologia.sisem.domain.model.header

import androidx.compose.ui.Modifier

data class HeaderModel(
    val title: TextModel,
    val subtitle: TextModel?,
    val leftIcon: String?,
    val rightIcon: String?,
    val modifier: Modifier
)
