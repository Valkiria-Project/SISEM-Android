package com.skgtecnologia.sisem.ui.incident

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class IncidentNavigationModel(
    val back: Boolean = false,
    val stretcherRetentionAph: String? = null,
    val patientAph: String? = null
) : NavigationModel
