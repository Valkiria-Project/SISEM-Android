package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.model.ImageSelectionNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import timber.log.Timber

fun getAppStartDestination(model: StartupNavigationModel?): String {
    return if (model == null) {
        NavigationGraph.Auth.route
    } else when {
        model.isTurnStarted -> NavigationGraph.Main.route

        else -> NavigationGraph.Auth.route
    }
}

fun getAuthStartDestination(model: StartupNavigationModel?): String {
    return if (model?.requiresPreOperational == true) {
        AuthNavigationRoute.PreOperational.route
    } else {
        AuthNavigationRoute.AuthCards.route
    }
}

fun navigateToNextStep(navController: NavHostController, navigationModel: NavigationModel?) =
    when (navigationModel) {
        is ImageSelectionNavigationModel -> imageSelectionToNextStep(navController, navigationModel)
        is LoginNavigationModel -> loginToNextStep(navController, navigationModel)
        is PreOpNavigationModel -> preOpToNextStep(navController, navigationModel)
        else -> {}
    }

private fun imageSelectionToNextStep(
    navController: NavHostController,
    model: ImageSelectionNavigationModel
) = when {
    model.showCamera -> navController.navigate(CommonNavigationRoute.Camera.route)

    else -> Timber.d("no-op")
}

private fun loginToNextStep(
    navController: NavHostController,
    model: LoginNavigationModel
) = when {
    model.isWarning -> navController.navigate(AuthNavigationRoute.ChangePassword.route)

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
        navController.navigate(AuthNavigationRoute.PreOperational.route) {
            popUpTo(AuthNavigationRoute.AuthCards.route) {
                inclusive = true
            }
        }
    }

    else -> navController.navigate(AuthNavigationRoute.AuthCards.route)
}

private fun preOpToNextStep(
    navController: NavHostController,
    model: PreOpNavigationModel
) = when {
    model.isTurnComplete -> navController.navigate(NavigationGraph.Main.route) {
        popUpTo(AuthNavigationRoute.AuthCards.route) {
            inclusive = true
        }
    }

    model.isImageSelection -> navController.navigate(CommonNavigationRoute.ImageSelection.route)

    else -> navController.navigate(AuthNavigationRoute.AuthCards.route)
}
