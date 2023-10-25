package com.skgtecnologia.sisem.ui.deviceauth

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class DeviceAuthNavigationModel(
    val isCrewList: Boolean = false,
    val isCancel: Boolean = false,
    val from: String = ""
) : NavigationModel
