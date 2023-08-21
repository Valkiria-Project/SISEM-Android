package com.skgtecnologia.sisem.ui.navigation.model

import com.skgtecnologia.sisem.di.operation.OperationRole

data class StartupNavigationModel(
    val isTurnStarted: Boolean = false,
    val requiresPreOperational: Boolean = false,
    val preOperationRole: OperationRole? = null
) : NavigationModel
