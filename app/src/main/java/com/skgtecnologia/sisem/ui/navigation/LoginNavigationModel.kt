package com.skgtecnologia.sisem.ui.navigation

import com.skgtecnologia.sisem.di.operation.OperationRole

data class LoginNavigationModel(
    val isAdmin: Boolean = false,
    val isTurnComplete: Boolean = false,
    val requiresPreOperational: Boolean = false,
    val preOperationRole: OperationRole? = null,
    val requiresDeviceAuth: Boolean = false
) : NavigationModel
