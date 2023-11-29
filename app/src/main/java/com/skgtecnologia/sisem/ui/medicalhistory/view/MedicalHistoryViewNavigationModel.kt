package com.skgtecnologia.sisem.ui.medicalhistory.view

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class MedicalHistoryViewNavigationModel(
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val back: Boolean = false
) : NavigationModel
