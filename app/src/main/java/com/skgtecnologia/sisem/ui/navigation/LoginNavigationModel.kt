package com.skgtecnologia.sisem.ui.navigation

data class LoginNavigationModel(
    val isAdmin: Boolean = false,
    val isTurnComplete: Boolean = false,
    val requiresPreOperational: Boolean = false,
    val requiresDeviceAuth: Boolean = false,
) : NavigationModel
