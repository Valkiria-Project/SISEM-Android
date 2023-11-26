package com.skgtecnologia.sisem.ui.signature.init

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class InitSignatureNavigationModel(
    val back: Boolean = false,
    val document: String? = null
) : NavigationModel
