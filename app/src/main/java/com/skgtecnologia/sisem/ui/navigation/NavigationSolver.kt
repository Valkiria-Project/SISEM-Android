package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel

fun getAppStartDestination(model: StartupNavigationModel?): String {
    return if (model == null) {
        NavigationGraph.Auth.route
    } else when {
        model.isTurnStarted -> NavigationGraph.Main.route

        else -> NavigationGraph.Auth.route
    }
}

fun getAuthStartDestination(model: StartupNavigationModel?): String {
    return if (model == null) {
        NavigationGraph.Auth.route
    } else when {
        model.requiresPreOperational -> AuthNavigationRoute.PreOperational.route

        else -> NavigationGraph.Auth.route
    }
}

fun navigateToNextStep(navController: NavHostController, navigationModel: NavigationModel?) =
    when (navigationModel) {
        is LoginNavigationModel -> loginToNextStep(navController, navigationModel)
        else -> {}
    }

private fun loginToNextStep(
    navController: NavHostController,
    model: LoginNavigationModel
) = when {
    model.isAdmin && model.requiresDeviceAuth ->
        navController.navigate(AuthNavigationRoute.DeviceAuth.route)

    model.isAdmin && !model.requiresDeviceAuth ->
        navController.navigate(NavigationGraph.Main.route) {
            popUpTo(AuthNavigationRoute.AuthCards.route) {
                inclusive = true
            }
        }

    model.isTurnComplete && model.requiresPreOperational.not() ->
        navController.navigate(NavigationGraph.Main.route) {
            popUpTo(AuthNavigationRoute.AuthCards.route) {
                inclusive = true
            }
        }

    model.requiresPreOperational -> {
        val operationRole = model.preOperationRole?.name

        navController.navigate(AuthNavigationRoute.PreOperational.route) {
            popUpTo(AuthNavigationRoute.AuthCards.route) {
                inclusive = true
            }
        }
    }

    else -> navController.navigate(AuthNavigationRoute.AuthCards.route)
}
