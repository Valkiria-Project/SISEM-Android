package com.skgtecnologia.sisem.ui.navigation.model

import com.skgtecnologia.sisem.di.operation.OperationRole

data class LoginNavigationModel(
    val isWarning: Boolean = false,
    val isAdmin: Boolean = false,
    val isTurnComplete: Boolean = false,
    val requiresPreOperational: Boolean = false,
    val preOperationRole: OperationRole? = null,
    val requiresDeviceAuth: Boolean = false,
    val forgotPassword: Boolean = false
) : NavigationModel
