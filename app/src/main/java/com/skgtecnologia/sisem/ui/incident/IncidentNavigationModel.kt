package com.skgtecnologia.sisem.ui.incident

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class IncidentNavigationModel(
    val back: Boolean = false,
    val isAph: Boolean = false,
    val isStretcherRetention: Boolean = false,
    val patient: String? = null
) : NavigationModel
