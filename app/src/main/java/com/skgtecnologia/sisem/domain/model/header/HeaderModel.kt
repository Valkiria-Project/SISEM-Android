package com.skgtecnologia.sisem.domain.model.header

import androidx.compose.ui.Modifier

data class HeaderModel(
    val title: TextModel,
    val subtitle: TextModel? = null,
    val leftIcon: String? = null,
    val rightIcon: String? = null,
    val modifier: Modifier
)
