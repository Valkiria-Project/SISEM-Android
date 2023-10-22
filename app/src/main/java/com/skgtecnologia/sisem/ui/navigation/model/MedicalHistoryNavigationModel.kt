package com.skgtecnologia.sisem.ui.navigation.model

data class MedicalHistoryNavigationModel(
    val isInfoCardEvent: Boolean = false,
    val isMedsSelectorEvent: Boolean = false,
    val isSignatureEvent: Boolean = false
) : NavigationModel
