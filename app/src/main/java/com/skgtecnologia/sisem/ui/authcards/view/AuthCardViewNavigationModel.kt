package com.skgtecnologia.sisem.ui.authcards.view

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class AuthCardViewNavigationModel(
    val role: OperationRole? = null,
    val back: Boolean = false
) : NavigationModel
