package com.skgtecnologia.sisem.ui.navigation.model

data class PreOpNavigationModel(
    val isTurnComplete: Boolean = false,
    val isNewFinding: Boolean = false,
    val role: String? = null
) : NavigationModel
