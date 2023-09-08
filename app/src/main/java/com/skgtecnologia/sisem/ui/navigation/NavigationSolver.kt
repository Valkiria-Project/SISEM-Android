package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.model.DeviceAuthNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
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
        is DeviceAuthNavigationModel -> deviceAuthToNextStep(navController, navigationModel)
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

    model.isNewFinding ->
        navController.navigate("${ReportNavigationRoute.AddFindingScreen.route}/${model.role}")

    else -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route)
}

private fun reportToNextStep(
    navController: NavHostController,
    model: ReportNavigationModel
) {
    when {
        model.goBack || model.photoTaken -> navController.popBackStack()

        model.showCamera -> navController.navigate(ReportNavigationRoute.CameraScreen.route)

        model.saveFinding && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/finding"
        )

        model.saveReport && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/recordNews"
        )

        model.closeFinding -> navController.popBackStack(
            route = AuthNavigationRoute.PreOperationalScreen.route,
            inclusive = false
        )

        model.closeReport -> navController.navigate(NavigationGraph.Main.route) {
            popUpTo(MainNavigationRoute.AddReportRoleScreen.route) {
                inclusive = true
            }
        }
    }
}

fun deviceAuthToNextStep(
    navController: NavHostController,
    model: DeviceAuthNavigationModel
) {
    when {
        model.isAssociated -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route)
        model.isCancel -> navController.popBackStack()
        model.isCancelBanner -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route)
    }
}
