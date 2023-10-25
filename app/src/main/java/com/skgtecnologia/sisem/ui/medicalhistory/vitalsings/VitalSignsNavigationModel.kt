package com.skgtecnologia.sisem.ui.medicalhistory.vitalsings

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class VitalSignsNavigationModel(
    val goBack: Boolean = false,
    val values: Map<String, String>? = null
) : NavigationModel
