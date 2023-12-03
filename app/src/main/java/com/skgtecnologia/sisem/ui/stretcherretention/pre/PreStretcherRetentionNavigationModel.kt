package com.skgtecnologia.sisem.ui.stretcherretention.pre

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class PreStretcherRetentionNavigationModel(
    val back: Boolean = false,
    val patientAph: String? = null
) : NavigationModel
