package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController

fun navigateToNextStep(navController: NavHostController, navigationModel: NavigationModel?) =
    when (navigationModel) {
        is LoginNavigationModel -> navigateToLoginNextStep(navController, navigationModel)
        else -> {}
    }

private fun navigateToLoginNextStep(
    navController: NavHostController,
    loginNavigationModel: LoginNavigationModel
) = when {
    loginNavigationModel.isAdmin && loginNavigationModel.requiresDeviceAuth ->
        navController.navigate(AuthNavigationRoute.DeviceAuth.route)

    loginNavigationModel.isTurnComplete && loginNavigationModel.requiresPreOperational.not() ->
        navController.navigate(NavigationGraph.Menu.route) {
            popUpTo(AuthNavigationRoute.AuthCards.route) {
                inclusive = true
            }
        }

    loginNavigationModel.requiresPreOperational ->
        navController.navigate(AuthNavigationRoute.PreOperational.route) {
            popUpTo(AuthNavigationRoute.AuthCards.route) {
                inclusive = true
            }
        }

    else -> navController.navigate(AuthNavigationRoute.AuthCards.route)
}
