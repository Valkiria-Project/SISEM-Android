package com.skgtecnologia.sisem.ui.navigation.model

data class PreOpNavigationModel(
    val isTurnCompleteEvent: Boolean = false,
    val isNewFindingEvent: Boolean = false,
    val findingId: String? = null
) : NavigationModel
