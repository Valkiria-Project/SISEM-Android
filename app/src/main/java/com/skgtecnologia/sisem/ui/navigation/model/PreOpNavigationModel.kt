package com.skgtecnologia.sisem.ui.navigation.model

data class PreOpNavigationModel(
    val isTurnComplete: Boolean = false,
    val isImageSelection: Boolean = false
) : NavigationModel
