package com.skgtecnologia.sisem.ui.navigation.model

data class PreOpNavigationModel(
    val isTurnCompleteEvent: Boolean = false,
    val isNewFindingEvent: Boolean = false
) : NavigationModel
