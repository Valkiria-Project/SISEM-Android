package com.skgtecnologia.sisem.ui.navigation.model

data class DeviceAuthNavigationModel(
    val isAssociated: Boolean = false,
    val isCancel: Boolean = false,
    val isCancelBanner: Boolean = false
) : NavigationModel
