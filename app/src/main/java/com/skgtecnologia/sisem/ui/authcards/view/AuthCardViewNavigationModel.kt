package com.skgtecnologia.sisem.ui.authcards.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class AuthCardViewNavigationModel(
    val role: OperationRole? = null,
    val isPendingPreOperational: Boolean = false,
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()

            role != null && !isPendingPreOperational -> {
                navController.navigate(MainRoute.PreoperationalViewRoute(role.name))
            }

            role != null && isPendingPreOperational -> {
                navController.navigate(
                    AuthRoute.PreOperationalRoute(role.name)
                )
            }
        }
    }
}
