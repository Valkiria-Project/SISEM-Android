package com.skgtecnologia.sisem.ui.navigation.model

import android.os.Parcelable
import com.skgtecnologia.sisem.di.operation.OperationRole
import kotlinx.parcelize.Parcelize

@Parcelize
data class StartupNavigationModel(
    val isTurnStarted: Boolean = false,
    val requiresPreOperational: Boolean = false,
    val preOperationRole: OperationRole? = null
) : NavigationModel, Parcelable
