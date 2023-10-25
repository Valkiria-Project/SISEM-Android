package com.skgtecnologia.sisem.ui.login

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class LoginNavigationModel(
    val isWarning: Boolean = false,
    val isAdmin: Boolean = false,
    val isTurnComplete: Boolean = false,
    val requiresPreOperational: Boolean = false,
    val preOperationRole: OperationRole? = null,
    val requiresDeviceAuth: Boolean = false,
    val forgotPassword: Boolean = false
) : NavigationModel
