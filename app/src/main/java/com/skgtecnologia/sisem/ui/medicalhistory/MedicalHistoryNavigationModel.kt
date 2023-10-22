package com.skgtecnologia.sisem.ui.medicalhistory

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class MedicalHistoryNavigationModel(
    val isInfoCardEvent: Boolean = false,
    val isMedsSelectorEvent: Boolean = false,
    val isSignatureEvent: Boolean = false
) : NavigationModel
