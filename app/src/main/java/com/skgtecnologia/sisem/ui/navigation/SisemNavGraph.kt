package com.skgtecnologia.sisem.ui.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.commons.communication.AppEvent
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.commons.location.ACTION_START
import com.skgtecnologia.sisem.commons.location.LocationService
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
import com.skgtecnologia.sisem.ui.medicalhistory.camera.CameraScreen
import com.skgtecnologia.sisem.ui.medicalhistory.camera.CameraViewScreen
import com.skgtecnologia.sisem.ui.medicalhistory.create.MedicalHistoryScreen
import com.skgtecnologia.sisem.ui.medicalhistory.medicine.MedicineScreen
import com.skgtecnologia.sisem.ui.medicalhistory.signaturepad.SignaturePadScreen
import com.skgtecnologia.sisem.ui.medicalhistory.view.MedicalHistoryViewScreen
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
import com.skgtecnologia.sisem.ui.sendemail.SendEmailScreen
import com.skgtecnologia.sisem.ui.signature.init.InitSignatureScreen
import com.skgtecnologia.sisem.ui.signature.view.SignatureScreen
import com.skgtecnologia.sisem.ui.stretcherretention.create.StretcherRetentionScreen
import com.skgtecnologia.sisem.ui.stretcherretention.pre.PreStretcherRetentionScreen
import com.skgtecnologia.sisem.ui.stretcherretention.view.StretcherRetentionViewScreen
import kotlin.reflect.typeOf

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
            if (appEvent is AppEvent.UnauthorizedSession) {
                navController.navigate(AuthRoute.LoginRoute(appEvent.username)) {
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
            aphGraph(navController, modifier)
            reportGraph(navController, modifier)
        }
    }
}

@Suppress("LongMethod")
private fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    startDestination: AuthRoute,
    modifier: Modifier,
    context: Context
) {
    navigation<NavGraph.AuthGraph>(startDestination = startDestination) {
        composable<AuthRoute.AuthCardsRoute> {
            AuthCardsScreen(
                modifier = modifier
            ) {
                navController.navigate(AuthRoute.LoginRoute())
            }
        }

        composable<AuthRoute.LoginRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: AuthRoute.LoginRoute = backStackEntry.toRoute()

            LoginScreen(
                modifier = modifier,
                previousUsername = route.username
            ) { navigationModel ->
                with(navigationModel) {
                    if (isTurnComplete && requiresPreOperational.not()) {
                        Intent(context.applicationContext, LocationService::class.java).apply {
                            action = ACTION_START
                            context.startService(this)
                        }
                    }
                    navigate(navController)
                }
            }
        }

        composable<AuthRoute.ForgotPasswordRoute> {
            ForgotPasswordScreen(
                modifier = modifier,
                onNavigation = { navigationModel ->
                    navigationModel.navigate(navController)
                }
            )
        }

        composable<AuthRoute.DeviceAuthRoute>(
//            route = AuthNavigationRoute.DeviceAuthScreen.route +
//                "/{${NavigationArgument.FROM}}",
//            arguments = listOf(navArgument(NavigationArgument.FROM) { type = NavType.StringType })
        ) { backStackEntry ->
            val route: AuthRoute.DeviceAuthRoute = backStackEntry.toRoute()

            DeviceAuthScreen(
                from = backStackEntry.arguments?.getString(NavigationArgument.FROM).orEmpty(),
                modifier = modifier
            ) { navigationModel ->
                with(navigationModel) {
                    if (isCancel && from != MAIN) {
                        if (!navController.navigateBack()) (context as Activity).finish()
                    } else {
                        navigationModel.navigate(navController)
                    }
                }
            }
        }

        composable<AuthRoute.PreOperationalRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: AuthRoute.PreOperationalRoute = backStackEntry.toRoute()

            val revertFinding = backStackEntry.savedStateHandle.get<Boolean>(REVERT_FINDING)
            backStackEntry.savedStateHandle.remove<Boolean>(REVERT_FINDING)

            val novelty = backStackEntry.savedStateHandle.get<Novelty>(NOVELTY)
            backStackEntry.savedStateHandle.remove<Novelty>(NOVELTY)

            PreOperationalScreen(
                modifier = modifier,
                roleName = route.role,
                novelty = novelty,
                revertFinding = revertFinding
            ) { navigationModel ->
                with(navigationModel) {
                    if (isTurnComplete) {
                        Intent(context.applicationContext, LocationService::class.java).apply {
                            action = ACTION_START
                            context.startService(this)
                        }
                    }
                    navigate(navController)
                }
            }
        }

        composable<AuthRoute.ChangePasswordRoute> {
            ChangePasswordScreen(
                modifier = modifier,
                onNavigation = { navigationModel ->
                    with(navigationModel) {
                        navigate(navController)
                    }
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
                    navController.navigate(route = navigationRoute.route)
                },
                onAction = { aphRoute ->
                    navController.navigate(aphRoute)
                },
                onLogout = {
                    navController.navigate(AuthRoute.AuthCardsRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }

                        launchSingleTop = true
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
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = MainNavigationRoute.InventoryScreen.route
        ) {
            InventoryScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
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
                navigationModel.navigate(navController)
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
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = MainNavigationRoute.DeviceAuthScreen.route
        ) {
            navController.navigate(AuthRoute.DeviceAuthRoute)
//                "${AuthNavigationRoute.DeviceAuthScreen.route}/$MAIN")
        }

        composable(
            route = MainNavigationRoute.InitSignatureScreen.route
        ) {
            InitSignatureScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
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
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = MainNavigationRoute.PreStretcherRetentionScreen.route
        ) {
            PreStretcherRetentionScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = MainNavigationRoute.StretcherRetentionScreen.route + "/{$ID_APH}",
            arguments = listOf(navArgument(ID_APH) { type = NavType.IntType })
        ) {
            StretcherRetentionScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = MainNavigationRoute.StretcherViewScreen.route + "/{$ID_APH}",
            arguments = listOf(navArgument(ID_APH) { type = NavType.IntType })
        ) {
            StretcherRetentionViewScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = MainNavigationRoute.PreOperationalViewScreen.route +
                "?$ROLE={$ROLE}",
            arguments = listOf(navArgument(ROLE) { type = NavType.StringType })
        ) {
            PreOperationalViewScreen { navigationModel ->
                navigationModel.navigate(navController)
            }
        }
    }
}

@Suppress("LongMethod")
private fun NavGraphBuilder.aphGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    navigation(
        startDestination = AphNavigationRoute.MedicalHistoryScreen.route,
        route = NavigationGraph.Aph.route
    ) {
        composable(
            route = AphNavigationRoute.MedicalHistoryScreen.route + "/{$ID_APH}",
            arguments = listOf(navArgument(ID_APH) { type = NavType.IntType })
        ) { navBackStackEntry ->
            val vitalSigns =
                navBackStackEntry.savedStateHandle.get<Map<String, String>>(VITAL_SIGNS)
            navBackStackEntry.savedStateHandle.remove<Map<String, String>>(VITAL_SIGNS)

            val medicine = navBackStackEntry.savedStateHandle.get<Map<String, String>>(MEDICINE)
            navBackStackEntry.savedStateHandle.remove<Map<String, String>>(MEDICINE)

            val signature = navBackStackEntry.savedStateHandle.get<String>(SIGNATURE)
            navBackStackEntry.savedStateHandle.remove<String>(SIGNATURE)

            val photoTaken = navBackStackEntry.savedStateHandle.get<Boolean>(PHOTO_TAKEN)
            navBackStackEntry.savedStateHandle.remove<Boolean>(PHOTO_TAKEN)

            MedicalHistoryScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                modifier = modifier,
                vitalSigns = vitalSigns,
                medicine = medicine,
                signature = signature,
                photoTaken = photoTaken == true
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = AphNavigationRoute.MedicalHistoryViewScreen.route + "/{$ID_APH}",
            arguments = listOf(navArgument(ID_APH) { type = NavType.IntType })
        ) { navBackStackEntry ->
            val photoTaken = navBackStackEntry.savedStateHandle.get<Boolean>(PHOTO_TAKEN)
            navBackStackEntry.savedStateHandle.remove<Boolean>(PHOTO_TAKEN)

            MedicalHistoryViewScreen(
                modifier = modifier,
                photoTaken = photoTaken == true
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = AphNavigationRoute.CameraScreen.route
        ) { navBackStackEntry ->
            CameraScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = AphNavigationRoute.CameraViewScreen.route
        ) { navBackStackEntry ->
            CameraViewScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = AphNavigationRoute.MedicineScreen.route
        ) {
            MedicineScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = AphNavigationRoute.SendEmailScreen.route + "/{$ID_APH}",
            arguments = listOf(navArgument(ID_APH) { type = NavType.IntType })
        ) {
            SendEmailScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = AphNavigationRoute.SignaturePadScreen.route
        ) {
            SignaturePadScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = AphNavigationRoute.VitalSignsScreen.route
        ) {
            VitalSignsScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
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
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = ReportNavigationRoute.ReportCameraScreen.route
        ) { navBackStackEntry ->
            ReportCameraScreen(
                modifier = modifier,
                viewModel = navBackStackEntry.sharedViewModel(navController = navController)
            ) { navigationModel ->
                navigationModel.navigate(navController)
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
                navigationModel.navigate(navController)
            }
        }

        composable(
            route = ReportNavigationRoute.AddReportRoleScreen.route
        ) {
            AddReportRoleScreen(
                modifier = modifier,
                onNavigation = { roleName ->
                    navController.navigate(
                        ReportNavigationRoute.AddReportScreen.route + "?$ROLE=$roleName"
                    )
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
            route = ReportNavigationRoute.AddReportScreen.route +
                "?$ROLE={$ROLE}",
            arguments = listOf(
                navArgument(ROLE) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { navBackStackEntry ->
            AddReportScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                onNavigation = { navigationModel ->
                    navigationModel.navigate(navController)
                }
            )
        }
    }
}
