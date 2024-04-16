package com.skgtecnologia.sisem.ui.login

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.LOGIN
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

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
            isWarning -> navController.navigate(AuthNavigationRoute.ChangePasswordScreen.route)
            isAdmin && requiresDeviceAuth ->
                navController.navigate("${AuthNavigationRoute.DeviceAuthScreen.route}/$LOGIN")

            isAdmin && !requiresDeviceAuth ->
                navController.navigate(NavigationGraph.Main.route) {
                    popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                        inclusive = true
                    }
                }

            isTurnComplete && requiresPreOperational.not() ->
                navController.navigate(NavigationGraph.Main.route) {
                    popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                        inclusive = true
                    }
                }

            requiresPreOperational -> {
                navController.navigate(AuthNavigationRoute.PreOperationalScreen.route) {
                    popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                        inclusive = true
                    }
                }
            }

            forgotPassword -> navController.navigate(AuthNavigationRoute.ForgotPasswordScreen.route)

            else -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route)
        }
    }
}
