package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
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
        AuthNavigationRoute.PreOperationalScreen.route
    } else {
        AuthNavigationRoute.AuthCardsScreen.route
    }
}

fun navigateToNextStep(navController: NavHostController, navigationModel: NavigationModel?) =
    when (navigationModel) {
        is LoginNavigationModel -> loginToNextStep(navController, navigationModel)
        is PreOpNavigationModel -> preOpToNextStep(navController, navigationModel)
        is ReportNavigationModel -> reportToNextStep(navController, navigationModel)
        else -> {}
    }

private fun loginToNextStep(
    navController: NavHostController,
    model: LoginNavigationModel
) = when {
    model.isWarning -> navController.navigate(AuthNavigationRoute.ChangePasswordScreen.route)

    model.isAdmin && model.requiresDeviceAuth ->
        navController.navigate(AuthNavigationRoute.DeviceAuthScreen.route)

    model.isAdmin && !model.requiresDeviceAuth ->
        navController.navigate(NavigationGraph.Main.route) {
            popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                inclusive = true
            }
        }

    model.isTurnComplete && model.requiresPreOperational.not() ->
        navController.navigate(NavigationGraph.Main.route) {
            popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                inclusive = true
            }
        }

    model.requiresPreOperational -> {
        navController.navigate(AuthNavigationRoute.PreOperationalScreen.route) {
            popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                inclusive = true
            }
        }
    }

    else -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route)
}

private fun preOpToNextStep(
    navController: NavHostController,
    model: PreOpNavigationModel
) = when {
    model.isTurnComplete -> navController.navigate(NavigationGraph.Main.route) {
        popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
            inclusive = true
        }
    }

    model.isNewFinding -> navController.navigate(ReportNavigationRoute.FindingsScreen.route)

    else -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route)
}

private fun reportToNextStep(
    navController: NavHostController,
    model: ReportNavigationModel
) {
    when {
        model.goBack || model.photoTaken -> navController.popBackStack()

        model.showCamera -> navController.navigate(ReportNavigationRoute.CameraScreen.route)

        model.confirmMedia -> Timber.d("Finish this")

        model.saveFinding && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/finding"
        )

        model.saveFinding -> navController.popBackStack()

        model.saveRecordNews && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/recordNews"
        )

        model.closeReport -> navController.navigate(NavigationGraph.Main.route) {
            popUpTo(MainNavigationRoute.AddReportRoleScreen.route) {
                inclusive = true
            }
        }
    }
}
