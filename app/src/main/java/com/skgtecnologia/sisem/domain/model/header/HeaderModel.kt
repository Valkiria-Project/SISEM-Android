package com.skgtecnologia.sisem.domain.model.header

import androidx.compose.ui.Modifier

data class HeaderModel(
    val title: Title,
    val subtitle: Subtitle?,
    val leftIcon: String?,
    val modifier: Modifier
)
