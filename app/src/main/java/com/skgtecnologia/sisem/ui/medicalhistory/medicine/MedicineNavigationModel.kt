package com.skgtecnologia.sisem.ui.medicalhistory.medicine

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class MedicineNavigationModel(
    val goBack: Boolean = false,
    val values: Map<String, String>? = null
) : NavigationModel
