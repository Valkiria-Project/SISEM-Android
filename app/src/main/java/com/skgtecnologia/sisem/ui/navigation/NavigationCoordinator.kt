@file:Suppress("TooManyFunctions")

package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.authcards.view.AuthCardViewNavigationModel
import com.skgtecnologia.sisem.ui.deviceauth.DeviceAuthNavigationModel
import com.skgtecnologia.sisem.ui.forgotpassword.ForgotPasswordNavigationModel
import com.skgtecnologia.sisem.ui.incident.IncidentNavigationModel
import com.skgtecnologia.sisem.ui.inventory.InventoryNavigationModel
import com.skgtecnologia.sisem.ui.inventory.view.InventoryViewNavigationModel
import com.skgtecnologia.sisem.ui.login.LoginNavigationModel
import com.skgtecnologia.sisem.ui.medicalhistory.MedicalHistoryNavigationModel
import com.skgtecnologia.sisem.ui.medicalhistory.medicine.MedicineNavigationModel
import com.skgtecnologia.sisem.ui.medicalhistory.signaturepad.SignaturePadNavigationModel
import com.skgtecnologia.sisem.ui.medicalhistory.view.MedicalHistoryViewNavigationModel
import com.skgtecnologia.sisem.ui.medicalhistory.vitalsings.VitalSignsNavigationModel
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.DOCUMENT
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.INVENTORY_TYPE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.MEDICINE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.NOVELTY
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.PHOTO_TAKEN
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.REVERT_FINDING
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.ROLE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.SIGNATURE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.VITAL_SIGNS
import com.skgtecnologia.sisem.ui.preoperational.create.PreOpNavigationModel
import com.skgtecnologia.sisem.ui.preoperational.view.PreOpViewNavigationModel
import com.skgtecnologia.sisem.ui.report.ReportNavigationModel
import com.skgtecnologia.sisem.ui.sendemail.SendEmailNavigationModel
import com.skgtecnologia.sisem.ui.signature.init.InitSignatureNavigationModel
import com.skgtecnologia.sisem.ui.signature.view.SignatureNavigationModel
import com.skgtecnologia.sisem.ui.stretcherretention.create.StretcherRetentionNavigationModel
import com.skgtecnologia.sisem.ui.stretcherretention.pre.PreStretcherRetentionNavigationModel
import com.skgtecnologia.sisem.ui.stretcherretention.view.StretcherRetentionViewNavigationModel

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

@Suppress("ComplexMethod")
fun navigateToNextStep(
    navController: NavHostController,
    navigationModel: NavigationModel?,
    onNavigationFallback: () -> Unit = {}
) = when (navigationModel) {
    is AuthCardViewNavigationModel -> authCardViewNextStep(navController, navigationModel)
    is DeviceAuthNavigationModel ->
        deviceAuthToNextStep(navController, navigationModel, onNavigationFallback)

    is ForgotPasswordNavigationModel -> forgotPasswordToNextStep(navController, navigationModel)
    is IncidentNavigationModel -> incidentToNextStep(navController, navigationModel)
    is InitSignatureNavigationModel -> initSignatureToNextStep(navController, navigationModel)
    is InventoryNavigationModel -> inventoryToNextStep(navController, navigationModel)
    is InventoryViewNavigationModel -> inventoryViewToNextStep(navController, navigationModel)
    is LoginNavigationModel -> loginToNextStep(navController, navigationModel)
    is MedicalHistoryNavigationModel -> medicalHistoryToNextStep(navController, navigationModel)
    is MedicalHistoryViewNavigationModel ->
        medicalHistoryViewToNextStep(navController, navigationModel)

    is MedicineNavigationModel -> medicineToNextStep(navController, navigationModel)
    is PreOpViewNavigationModel -> preOpViewToNextStep(navController, navigationModel)
    is PreOpNavigationModel -> preOpToNextStep(navController, navigationModel)
    is ReportNavigationModel -> reportToNextStep(navController, navigationModel)
    is SendEmailNavigationModel -> sendEmailToNextStep(navController, navigationModel)
    is SignatureNavigationModel -> signatureToNextStep(navController, navigationModel)
    is SignaturePadNavigationModel -> signaturePadToNextStep(navController, navigationModel)
    is PreStretcherRetentionNavigationModel ->
        preStretcherRetentionToNextStep(navController, navigationModel)

    is StretcherRetentionNavigationModel ->
        stretcherRetentionToNextStep(navController, navigationModel)

    is StretcherRetentionViewNavigationModel ->
        stretcherRetentionViewToNextStep(navController, navigationModel)

    is VitalSignsNavigationModel -> vitalSignsToNextStep(navController, navigationModel)
    else -> {}
}

fun authCardViewNextStep(
    navController: NavHostController,
    model: AuthCardViewNavigationModel
) {
    when {
        model.back -> navController.popBackStack()

        model.role != null -> {
            navController.navigate(
                MainNavigationRoute.PreOperationalViewScreen.route + "?$ROLE=${model.role.name}"
            )
        }
    }
}

private fun deviceAuthToNextStep(
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

fun forgotPasswordToNextStep(
    navController: NavHostController,
    model: ForgotPasswordNavigationModel
) {
    when {
        model.isCancel || model.isSuccess -> navController.popBackStack()
    }
}

fun incidentToNextStep(
    navController: NavHostController,
    model: IncidentNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
        model.patientAph != null -> navController.navigate(
            AphNavigationRoute.MedicalHistoryViewScreen.route + "/${model.patientAph}"
        )

        model.stretcherRetentionAph != null -> navController.navigate(
            MainNavigationRoute.StretcherViewScreen.route + "/${model.stretcherRetentionAph}"
        )
    }
}

fun initSignatureToNextStep(
    navController: NavHostController,
    model: InitSignatureNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
        model.document?.isNotEmpty() == true -> navController.navigate(
            MainNavigationRoute.SignatureScreen.route + "?$DOCUMENT=${model.document}"
        )
    }
}

fun inventoryToNextStep(
    navController: NavHostController,
    model: InventoryNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
        model.identifier != null -> navController.navigate(
            MainNavigationRoute.InventoryViewScreen.route + "?$INVENTORY_TYPE=${model.identifier}"
        )
    }
}

fun inventoryViewToNextStep(
    navController: NavHostController,
    model: InventoryViewNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
    }
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

private fun medicalHistoryToNextStep(
    navController: NavHostController,
    model: MedicalHistoryNavigationModel
) {
    when {
        model.back -> navController.popBackStack()

        model.isInfoCardEvent -> navController.navigate(AphNavigationRoute.VitalSignsScreen.route)

        model.isMedsSelectorEvent ->
            navController.navigate(AphNavigationRoute.MedicineScreen.route)

        model.isSignatureEvent ->
            navController.navigate(AphNavigationRoute.SignaturePadScreen.route)

        model.showCamera -> navController.navigate(AphNavigationRoute.CameraScreen.route)
        model.photoTaken -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(PHOTO_TAKEN, true)
        }
    }
}

private fun medicalHistoryViewToNextStep(
    navController: NavHostController,
    model: MedicalHistoryViewNavigationModel
) {
    when {
        model.back -> navController.popBackStack()

        model.sendMedical -> navController.navigate(MainNavigationRoute.SendEmailScreen.route)

        model.showCamera -> navController.navigate(AphNavigationRoute.CameraScreen.route)

        model.photoTaken -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(PHOTO_TAKEN, true)
        }
    }
}

private fun medicineToNextStep(
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

fun preOpViewToNextStep(
    navController: NavHostController,
    model: PreOpViewNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
    }
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
        model.showCamera -> navController.navigate(ReportNavigationRoute.ReportCameraScreen.route)
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

fun sendEmailToNextStep(
    navController: NavHostController,
    model: SendEmailNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
        model.send -> navController.navigate(NavigationGraph.Main.route)
    }
}

fun signatureToNextStep(
    navController: NavHostController,
    model: SignatureNavigationModel
) {
    when {
        model.back -> navController.popBackStack()

        model.isSignatureEvent ->
            navController.navigate(AphNavigationRoute.SignaturePadScreen.route)

        // FIXME: validar si no se devuelve a la pantalla de firma
        model.isSaved -> navController.navigate(NavigationGraph.Main.route)
    }
}

private fun signaturePadToNextStep(
    navController: NavHostController,
    model: SignaturePadNavigationModel
) {
    when {
        model.goBack -> navController.popBackStack()

        model.signature != null -> with(navController) {
            popBackStack()

            currentBackStackEntry
                ?.savedStateHandle
                ?.set(SIGNATURE, model.signature)
        }
    }
}

fun preStretcherRetentionToNextStep(
    navController: NavHostController,
    model: PreStretcherRetentionNavigationModel
) {
    when {
        model.back -> navController.popBackStack()

        model.patientAph != null -> navController.navigate(
            MainNavigationRoute.StretcherRetentionScreen.route + "/${model.patientAph}"
        )
    }
}

fun stretcherRetentionToNextStep(
    navController: NavHostController,
    model: StretcherRetentionNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
    }
}

fun stretcherRetentionViewToNextStep(
    navController: NavHostController,
    model: StretcherRetentionViewNavigationModel
) {
    when {
        model.back -> navController.popBackStack()
    }
}

private fun vitalSignsToNextStep(
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
