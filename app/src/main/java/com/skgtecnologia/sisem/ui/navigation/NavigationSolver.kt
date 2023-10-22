package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.MEDICINE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.NOVELTY
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.REVERT_FINDING
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.VITAL_SIGNS
import com.skgtecnologia.sisem.ui.navigation.model.DeviceAuthNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.MedicalHistoryNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.MedicineNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import com.skgtecnologia.sisem.ui.navigation.model.VitalSignsNavigationModel

const val APP_STARTED = "app_started"
const val FINDING = "finding"
const val LOGIN = "login"
const val MAIN = "main"
const val REPORT = "report"

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
    model?.isWarning == true -> AuthNavigationRoute.ChangePasswordScreen.route
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
        is DeviceAuthNavigationModel ->
            deviceAuthToNextStep(navController, navigationModel, onNavigationFallback)

        is LoginNavigationModel -> loginToNextStep(navController, navigationModel)
        is MedicalHistoryNavigationModel -> medicalHistoryToNextStep(navController, navigationModel)
        is MedicineNavigationModel -> medicineToNextStep(navController, navigationModel)
        is PreOpNavigationModel -> preOpToNextStep(navController, navigationModel)
        is ReportNavigationModel -> reportToNextStep(navController, navigationModel)
        is VitalSignsNavigationModel -> vitalSignsToNextStep(navController, navigationModel)
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
    model.isTurnCompleteEvent -> navController.navigate(NavigationGraph.Main.route) {
        popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
            inclusive = true
        }
    }

    model.isNewFindingEvent ->
        navController.navigate(
            ReportNavigationRoute.AddFindingScreen.route +
                    "?${NavigationArgument.FINDING_ID}=${model.findingId}"
        )

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
        model.goBackFromReport -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(REVERT_FINDING, true)
        }

        model.goBackFromImages -> navController.popBackStack()
        model.showCamera -> navController.navigate(ReportNavigationRoute.CameraScreen.route)
        model.photoTaken -> navController.popBackStack()
        model.closeFinding && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/$FINDING"
        )

        model.closeFinding -> with(navController) {
            popBackStack(
                route = AuthNavigationRoute.PreOperationalScreen.route,
                inclusive = false
            )

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(NOVELTY, model.novelty)
        }

        model.closeReport && model.imagesSize > 0 -> navController.navigate(
            "${ReportNavigationRoute.ImagesConfirmationScreen.route}/$REPORT"
        )

        model.closeReport ->
            navController.navigate(NavigationGraph.Main.route) {
                popUpTo(ReportNavigationRoute.AddReportRoleScreen.route) {
                    inclusive = true
                }
            }
    }
}

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

        model.isCancel && model.from == MAIN ->
            navController.navigate(NavigationGraph.Main.route) {
                popUpTo(AuthNavigationRoute.DeviceAuthScreen.route) {
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

fun medicalHistoryToNextStep(
    navController: NavHostController,
    model: MedicalHistoryNavigationModel
) {
    when {
        model.isInfoCardEvent -> navController.navigate(MainNavigationRoute.VitalSignsScreen.route)

        model.isMedsSelectorEvent ->
            navController.navigate(MainNavigationRoute.MedicineScreen.route)

        model.isSignatureEvent ->
            navController.navigate(MainNavigationRoute.SignaturePadScreen.route)
    }
}

fun vitalSignsToNextStep(
    navController: NavHostController,
    model: VitalSignsNavigationModel
) {
    when {
        model.goBack -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(VITAL_SIGNS, null)
        }

        model.values != null -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(VITAL_SIGNS, model.values)
        }
    }
}

fun medicineToNextStep(
    navController: NavHostController,
    model: MedicineNavigationModel
) {
    when {
        model.goBack -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(MEDICINE, null)
        }

        model.values != null -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(MEDICINE, model.values)
        }
    }
}
