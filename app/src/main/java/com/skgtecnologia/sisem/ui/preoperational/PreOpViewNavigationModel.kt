package com.skgtecnologia.sisem.ui.preoperational

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class PreOpViewNavigationModel(
    val role: OperationRole? = null,
    val back: Boolean = false
) : NavigationModel
