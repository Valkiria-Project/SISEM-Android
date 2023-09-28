package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.REVERT_FINDING
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
        model.isAdmin && !model.vehicleCode.isNullOrEmpty() -> NavigationGraph.Main.route
        model.isTurnStarted -> NavigationGraph.Main.route
        else -> NavigationGraph.Auth.route
    }
}

fun getAuthStartDestination(model: StartupNavigationModel?): String = when {
    model?.isAdmin == true -> "${AuthNavigationRoute.DeviceAuthScreen.route}/$APP_STARTED"
    model?.requiresPreOperational == true -> AuthNavigationRoute.PreOperationalScreen.route
    else -> AuthNavigationRoute.AuthCardsScreen.route
}

fun navigateToNextStep(
    navController: NavHostController,
    navigationModel: NavigationModel?,
    onNavigationFallback: () -> Unit = {}
) =
    when (navigationModel) {
        is LoginNavigationModel -> loginToNextStep(navController, navigationModel)
        is PreOpNavigationModel -> preOpToNextStep(navController, navigationModel)
        is ReportNavigationModel -> reportToNextStep(navController, navigationModel)
        is DeviceAuthNavigationModel ->
            deviceAuthToNextStep(navController, navigationModel, onNavigationFallback)

        else -> {}
    }

private fun loginToNextStep(
    navController: NavHostController,
    model: LoginNavigationModel
) = when {
    model.isWarning -> navController.navigate(AuthNavigationRoute.ChangePasswordScreen.route)
    model.isAdmin && model.requiresDeviceAuth ->
        navController.navigate("${AuthNavigationRoute.DeviceAuthScreen.route}/$LOGIN")

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

    model.forgotPassword -> navController.navigate(AuthNavigationRoute.ForgotPasswordScreen.route)

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
        navController.navigate(ReportNavigationRoute.AddFindingScreen.route)

    else -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
        popUpTo(AuthNavigationRoute.PreOperationalScreen.route) {
            inclusive = true
        }
    }
}

private fun reportToNextStep(
    navController: NavHostController,
    model: ReportNavigationModel
) {
    when {
        model.goBack -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(REVERT_FINDING, true)
        }

        model.photoTaken -> navController.popBackStack()
        model.showCamera -> navController.navigate(ReportNavigationRoute.CameraScreen.route)
        model.closeFinding && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/finding"
        )

        model.closeFinding -> navController.popBackStack(
            route = AuthNavigationRoute.PreOperationalScreen.route,
            inclusive = false
        )

        model.closeReport && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/recordNews"
        )

        model.closeReport ->
            navController.navigate(NavigationGraph.Main.route) {
                popUpTo(ReportNavigationRoute.AddReportRoleScreen.route) {
                    inclusive = true
                }
            }
    }
}

const val LOGIN = "LOGIN"
const val MAIN = "MAIN"
const val APP_STARTED = "APP_STARTED"

fun deviceAuthToNextStep(
    navController: NavHostController,
    model: DeviceAuthNavigationModel,
    onNavigationFallback: () -> Unit = {}
) {
    when {
        model.isCrewList && model.from == LOGIN ->
            navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                    inclusive = true
                }
            }

        // FIXME: revisit this logic, back is navigated to DeviceAuthScreen
        model.isCrewList && model.from == "" ->
            navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                popUpTo(AuthNavigationRoute.DeviceAuthScreen.route) {
                    inclusive = true
                }
            }

        model.isCrewList && model.from == MAIN ->
            navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                popUpTo(NavigationGraph.Main.route) {
                    inclusive = true
                }
            }

        model.isCancel -> {
            val goBack = navController.popBackStack()
            if (!goBack) {
                onNavigationFallback()
            }
        }
    }
}
