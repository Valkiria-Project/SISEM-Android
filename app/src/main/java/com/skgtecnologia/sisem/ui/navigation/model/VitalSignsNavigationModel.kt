package com.skgtecnologia.sisem.ui.navigation.model

data class VitalSignsNavigationModel(
    val goBack: Boolean = false,
    val confirmVitalSings: Boolean = false,
    val values: List<String>? = null
) : NavigationModel
