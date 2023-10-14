package com.skgtecnologia.sisem.ui.navigation.model

import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.model.MedicineModel

data class MedicineNavigationModel(
    val goBack: Boolean = false,
    val medicine: MedicineModel? = null
) : NavigationModel
