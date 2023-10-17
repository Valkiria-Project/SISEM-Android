package com.skgtecnologia.sisem.ui.navigation.model

data class VitalSignsNavigationModel(
    val goBack: Boolean = false,
    val values: Map<String, String>? = null
) : NavigationModel
