package com.skgtecnologia.sisem.ui.navigation

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.skgtecnologia.sisem.commons.communication.AppEvent
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.ui.authcards.create.AuthCardsScreen
import com.skgtecnologia.sisem.ui.authcards.view.AuthCardViewScreen
import com.skgtecnologia.sisem.ui.changepassword.ChangePasswordScreen
import com.skgtecnologia.sisem.ui.commons.extensions.sharedViewModel
import com.skgtecnologia.sisem.ui.deviceauth.DeviceAuthScreen
import com.skgtecnologia.sisem.ui.forgotpassword.ForgotPasswordScreen
import com.skgtecnologia.sisem.ui.incident.IncidentScreen
import com.skgtecnologia.sisem.ui.inventory.InventoryScreen
import com.skgtecnologia.sisem.ui.inventory.view.InventoryViewScreen
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.map.MapScreen
import com.skgtecnologia.sisem.ui.medicalhistory.MedicalHistoryScreen
import com.skgtecnologia.sisem.ui.medicalhistory.camera.CameraScreen
import com.skgtecnologia.sisem.ui.medicalhistory.medicine.MedicineScreen
import com.skgtecnologia.sisem.ui.medicalhistory.signaturepad.SignaturePadScreen
import com.skgtecnologia.sisem.ui.medicalhistory.vitalsings.VitalSignsScreen
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.DOCUMENT
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.ID_APH
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.INVENTORY_TYPE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.MEDICINE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.NOVELTY
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.PHOTO_TAKEN
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.REVERT_FINDING
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.ROLE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.SIGNATURE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.VITAL_SIGNS
import com.skgtecnologia.sisem.ui.preoperational.create.PreOperationalScreen
import com.skgtecnologia.sisem.ui.preoperational.view.PreOperationalViewScreen
import com.skgtecnologia.sisem.ui.report.addfinding.AddFindingScreen
import com.skgtecnologia.sisem.ui.report.addreport.AddReportRoleScreen
import com.skgtecnologia.sisem.ui.report.addreport.AddReportScreen
import com.skgtecnologia.sisem.ui.report.media.ImagesConfirmationScreen
import com.skgtecnologia.sisem.ui.report.media.ReportCameraScreen
import com.skgtecnologia.sisem.ui.signature.init.InitSignatureScreen
import com.skgtecnologia.sisem.ui.signature.view.SignatureScreen
import com.skgtecnologia.sisem.ui.stretcherretention.StretcherRetentionScreen

@Composable
fun SisemNavGraph(
    navigationModel: StartupNavigationModel?
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()
        val context = LocalContext.current

        UnauthorizedEventHandler.subscribeUnauthorizedEvent { appEvent ->
            if (appEvent == AppEvent.UNAUTHORIZED_SESSION) {
                navController.navigate(AuthNavigationRoute.LoginScreen.route) {
                    popUpTo(NavigationGraph.Main.route) {
                        inclusive = true
                    }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = getAppStartDestination(navigationModel)
        ) {
            authGraph(
                navController,
                getAuthStartDestination(navigationModel),
                modifier,
                context
            )
            mainGraph(navController, modifier)
            reportGraph(navController, modifier)
        }
    }
}

@Suppress("LongMethod")
private fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier,
    context: Context
) {
    navigation(
        startDestination = startDestination,
        route = NavigationGraph.Auth.route
    ) {
        composable(
            route = AuthNavigationRoute.AuthCardsScreen.route
        ) {
            AuthCardsScreen(
                modifier = modifier
            ) {
                navController.navigate(AuthNavigationRoute.LoginScreen.route)
            }
        }

        composable(
            route = AuthNavigationRoute.LoginScreen.route
        ) {
            LoginScreen(
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = AuthNavigationRoute.ForgotPasswordScreen.route
        ) {
            ForgotPasswordScreen(
                modifier = modifier,
                onNavigation = { navigationModel ->
                    navigateToNextStep(navController, navigationModel)
                }
            )
        }

        composable(
            route = AuthNavigationRoute.DeviceAuthScreen.route +
                "/{${NavigationArgument.FROM}}",
            arguments = listOf(navArgument(NavigationArgument.FROM) { type = NavType.StringType })
        ) {
            DeviceAuthScreen(
                from = it.arguments?.getString(NavigationArgument.FROM).orEmpty(),
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel) {
                    (context as Activity).finish()
                }
            }
        }

        composable(
            route = AuthNavigationRoute.PreOperationalScreen.route
        ) { navBackStackEntry ->
            val revertFinding = navBackStackEntry.savedStateHandle.get<Boolean>(REVERT_FINDING)
            navBackStackEntry.savedStateHandle.remove<Boolean>(REVERT_FINDING)

            val novelty = navBackStackEntry.savedStateHandle.get<Novelty>(NOVELTY)
            navBackStackEntry.savedStateHandle.remove<Novelty>(NOVELTY)

            PreOperationalScreen(
                modifier = modifier,
                novelty = novelty,
                revertFinding = revertFinding
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = AuthNavigationRoute.ChangePasswordScreen.route
        ) {
            ChangePasswordScreen(
                modifier = modifier,
                onNavigation = { navigationModel ->
                    navigateToNextStep(navController, navigationModel)
                },
                onCancel = { navController.navigateUp() }
            )
        }
    }
}

@Suppress("LongMethod")
private fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    navigation(
        startDestination = MainNavigationRoute.MapScreen.route,
        route = NavigationGraph.Main.route
    ) {
        composable(
            route = MainNavigationRoute.MapScreen.route
        ) {
            MapScreen(
                modifier = modifier,
                onMenuAction = { navigationRoute ->
                    navController.navigate(navigationRoute.route)
                },
                onAction = { aphRoute ->
                    navController.navigate(aphRoute)
                },
                onLogout = {
                    navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                        popUpTo(NavigationGraph.Main.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = MainNavigationRoute.IncidentScreen.route
        ) {
            IncidentScreen(
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.InventoryScreen.route
        ) {
            InventoryScreen(
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.InventoryViewScreen.route +
                "?$INVENTORY_TYPE={$INVENTORY_TYPE}",
            arguments = listOf(navArgument(INVENTORY_TYPE) { type = NavType.StringType })
        ) {
            InventoryViewScreen(
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.NotificationsScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MainNavigationRoute.DrivingGuideScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MainNavigationRoute.CertificationsScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MainNavigationRoute.HCEUDCScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MainNavigationRoute.ShiftScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MainNavigationRoute.PreoperationalMainScreen.route
        ) {
            AuthCardViewScreen { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.DeviceAuthScreen.route
        ) {
            navController.navigate("${AuthNavigationRoute.DeviceAuthScreen.route}/$MAIN")
        }

        composable(
            route = MainNavigationRoute.InitSignatureScreen.route
        ) {
            InitSignatureScreen(modifier = modifier) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.SignatureScreen.route + "?$DOCUMENT={$DOCUMENT}",
            arguments = listOf(navArgument(DOCUMENT) { type = NavType.StringType })
        ) { navBackStackEntry ->
            val signature = navBackStackEntry.savedStateHandle.get<String>(SIGNATURE)
            navBackStackEntry.savedStateHandle.remove<String>(SIGNATURE)

            SignatureScreen(
                modifier = modifier,
                signature = signature
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.VitalSignsScreen.route
        ) {
            VitalSignsScreen(modifier = modifier) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.MedicineScreen.route
        ) {
            MedicineScreen(modifier = modifier) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.SignaturePadScreen.route
        ) {
            SignaturePadScreen(modifier = modifier) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.CameraScreen.route
        ) { navBackStackEntry ->
            CameraScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = MainNavigationRoute.StretcherRetentionScreen.route
        ) {
            StretcherRetentionScreen(
                modifier = modifier
            ) {
                navigateToNextStep(navController, it)
            }
        }

        composable(
            route = MainNavigationRoute.PreOperationalViewScreen.route +
                "?$ROLE={$ROLE}",
            arguments = listOf(navArgument(ROLE) { type = NavType.StringType })
        ) {
            PreOperationalViewScreen { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }
    }
}

@Suppress("LongMethod")
private fun NavGraphBuilder.reportGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    navigation(
        startDestination = ReportNavigationRoute.AddFindingScreen.route,
        route = NavigationGraph.Report.route
    ) {
        composable(
            route = ReportNavigationRoute.AddFindingScreen.route +
                "?${NavigationArgument.FINDING_ID}={${NavigationArgument.FINDING_ID}}",
            arguments = listOf(
                navArgument(NavigationArgument.FINDING_ID) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            AddFindingScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                findingId = navBackStackEntry.arguments?.getString(NavigationArgument.FINDING_ID)
                    .orEmpty(),
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = ReportNavigationRoute.ReportCameraScreen.route
        ) { navBackStackEntry ->
            ReportCameraScreen(
                modifier = modifier,
                viewModel = navBackStackEntry.sharedViewModel(navController = navController)
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = ReportNavigationRoute.ImagesConfirmationScreen.route +
                "/{${NavigationArgument.FROM}}",
            arguments = listOf(navArgument(NavigationArgument.FROM) { type = NavType.StringType })
        ) { navBackStackEntry ->
            ImagesConfirmationScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                from = navBackStackEntry.arguments?.getString(NavigationArgument.FROM).orEmpty(),
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = ReportNavigationRoute.AddReportRoleScreen.route
        ) {
            AddReportRoleScreen(
                modifier = modifier,
                onNavigation = {
                    navController.navigate(ReportNavigationRoute.AddReportScreen.route)
                },
                onCancel = {
                    navController.navigate(NavigationGraph.Main.route) {
                        popUpTo(ReportNavigationRoute.AddReportRoleScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = ReportNavigationRoute.AddReportScreen.route,
        ) { navBackStackEntry ->
            AddReportScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                onNavigation = { navigationModel ->
                    navigateToNextStep(navController, navigationModel)
                }
            )
        }
    }
}
