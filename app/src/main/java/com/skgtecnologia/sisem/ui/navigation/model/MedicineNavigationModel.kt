package com.skgtecnologia.sisem.ui.navigation.model

data class MedicineNavigationModel(
    val goBack: Boolean = false,
    val values: Map<String, String>? = null
) : NavigationModel
