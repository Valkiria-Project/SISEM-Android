package com.skgtecnologia.sisem.ui.medicalhistory.signaturepad

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class SignaturePadNavigationModel(
    val goBack: Boolean = false,
    val signature: String? = null
) : NavigationModel
