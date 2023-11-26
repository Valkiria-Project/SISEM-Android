package com.skgtecnologia.sisem.ui.signature.view

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class SignatureNavigationModel(
    val isSignatureEvent: Boolean = false,
    val back: Boolean = false,
    val isSaved: Boolean = false
) : NavigationModel
