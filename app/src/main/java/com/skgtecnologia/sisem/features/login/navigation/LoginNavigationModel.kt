package com.skgtecnologia.sisem.features.login.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.core.navigation.AuthRoute
import com.skgtecnologia.sisem.core.navigation.LOGIN
import com.skgtecnologia.sisem.core.navigation.NavGraph
import com.skgtecnologia.sisem.core.navigation.NavigationModel
import com.skgtecnologia.sisem.di.operation.OperationRole

data class LoginNavigationModel(
    val isWarning: Boolean = false,
    val isAdmin: Boolean = false,
    val isTurnComplete: Boolean = false,
    val requiresPreOperational: Boolean = false,
    val preOperationRole: OperationRole? = null,
    val requiresDeviceAuth: Boolean = false,
    val forgotPassword: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            isWarning -> navController.navigate(AuthRoute.ChangePasswordRoute)
            isAdmin && requiresDeviceAuth -> navController.navigate(
                AuthRoute.DeviceAuthRoute(LOGIN)
            )

            isAdmin && !requiresDeviceAuth ->
                navController.navigate(NavGraph.MainGraph) {
                    popUpTo(AuthRoute.AuthCardsRoute) {
                        inclusive = true
                    }
                }

            isTurnComplete && requiresPreOperational.not() ->
                navController.navigate(NavGraph.MainGraph) {
                    popUpTo(AuthRoute.AuthCardsRoute) {
                        inclusive = true
                    }
                }

            requiresPreOperational -> {
                navController.navigate(AuthRoute.PreOperationalRoute()) {
                    popUpTo(AuthRoute.AuthCardsRoute) {
                        inclusive = true
                    }
                }
            }

            forgotPassword -> navController.navigate(AuthRoute.ForgotPasswordRoute)

            else -> navController.navigate(AuthRoute.AuthCardsRoute)
        }
    }
}
