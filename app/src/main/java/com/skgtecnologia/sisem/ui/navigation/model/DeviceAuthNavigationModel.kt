package com.skgtecnologia.sisem.ui.navigation.model

data class DeviceAuthNavigationModel(
    val isCrewList: Boolean = false,
    val isCancel: Boolean = false,
    val from: String = ""
) : NavigationModel
