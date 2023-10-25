package com.skgtecnologia.sisem.ui.preoperational

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class PreOpNavigationModel(
    val isTurnCompleteEvent: Boolean = false,
    val isNewFindingEvent: Boolean = false,
    val findingId: String? = null
) : NavigationModel
