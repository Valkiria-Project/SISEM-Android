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
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.MEDICINE
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.NOVELTY
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.PHOTO_TAKEN
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.REVERT_FINDING
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
fun SisemNavGraph(navigationModel: StartupNavigationModel?) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()
        val context = LocalContext.current

        UnauthorizedEventHandler.subscribeUnauthorizedEvent { appEvent ->
            if (appEvent is AppEvent.UnauthorizedSession) {
                navController.navigate(AuthRoute.LoginRoute(appEvent.username)) {
                    popUpTo(NavGraph.MainGraph) {
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
            ) { authRoute ->
                navController.navigate(authRoute)
            }
        }

        composable<AuthRoute.LoginRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) {
            LoginScreen(
                modifier = modifier,
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
            typeMap = mapOf(
                typeOf<String>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: AuthRoute.DeviceAuthRoute = backStackEntry.toRoute()

            DeviceAuthScreen(
                from = route.from,
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

            /*FIXME*/
            val revertFinding = backStackEntry.savedStateHandle.get<Boolean>(REVERT_FINDING)
            backStackEntry.savedStateHandle.remove<Boolean>(REVERT_FINDING)

            /*FIXME*/
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
    navigation<NavGraph.MainGraph>(startDestination = MainRoute.MapRoute) {
        composable<MainRoute.MapRoute> {
            MapScreen(
                modifier = modifier,
                onMenuAction = { navigationRoute ->
                    navController.navigate(route = navigationRoute)
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

        composable<MainRoute.IncidentsRoute> {
            IncidentScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.InventoryRoute> {
            InventoryScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.InventoryViewRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: MainRoute.InventoryViewRoute = backStackEntry.toRoute()

            InventoryViewScreen(
                modifier = modifier,
                inventoryName = route.inventoryName
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.NotificationsRoute> { /*FIXME: Finish this work*/ }

        composable<MainRoute.DrivingGuideRoute> { /*FIXME: Finish this work*/ }

        composable<MainRoute.CertificationsRoute> { /*FIXME: Finish this work*/ }

        composable<MainRoute.HCEUDCRoute> { /*FIXME: Finish this work*/ }

        composable<MainRoute.ShiftRoute> { /*FIXME: Finish this work*/ }

        composable<MainRoute.PreoperationalMainRoute> {
            AuthCardViewScreen { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.DeviceAuthMainRoute> {
            navController.navigate(AuthRoute.DeviceAuthRoute(MAIN))
        }

        composable<MainRoute.InitSignatureRoute> {
            InitSignatureScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.SignatureRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: MainRoute.SignatureRoute = backStackEntry.toRoute()

            val signature = backStackEntry.savedStateHandle.get<String>(SIGNATURE)
            backStackEntry.savedStateHandle.remove<String>(SIGNATURE)

            SignatureScreen(
                modifier = modifier,
                document = route.document,
                signature = signature
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.PreStretcherRetentionRoute> {
            PreStretcherRetentionScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.StretcherRetentionRoute>(
            typeMap = mapOf(
                typeOf<String>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: MainRoute.StretcherRetentionRoute = backStackEntry.toRoute()

            StretcherRetentionScreen(
                modifier = modifier,
                idAph = route.idAph
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.StretcherRetentionViewRoute>(
            typeMap = mapOf(
                typeOf<String>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: MainRoute.StretcherRetentionViewRoute = backStackEntry.toRoute()

            StretcherRetentionViewScreen(
                modifier = modifier,
                idAph = route.idAph
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.PreoperationalViewRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: MainRoute.PreoperationalViewRoute = backStackEntry.toRoute()

            PreOperationalViewScreen(
                modifier = modifier,
                roleName = route.role
            ) { navigationModel ->
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
    navigation<NavGraph.AphGraph>(
        startDestination = AphRoute.MedicalHistoryRoute::class,
    ) {
        composable<AphRoute.MedicalHistoryRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: AphRoute.MedicalHistoryRoute = backStackEntry.toRoute()

            val vitalSigns = backStackEntry.savedStateHandle.get<Map<String, String>>(VITAL_SIGNS)
            backStackEntry.savedStateHandle.remove<Map<String, String>>(VITAL_SIGNS)

            val medicine = backStackEntry.savedStateHandle.get<Map<String, String>>(MEDICINE)
            backStackEntry.savedStateHandle.remove<Map<String, String>>(MEDICINE)

            val signature = backStackEntry.savedStateHandle.get<String>(SIGNATURE)
            backStackEntry.savedStateHandle.remove<String>(SIGNATURE)

            val photoTaken = backStackEntry.savedStateHandle.get<Boolean>(PHOTO_TAKEN)
            backStackEntry.savedStateHandle.remove<Boolean>(PHOTO_TAKEN)

            MedicalHistoryScreen(
                modifier = modifier,
                viewModel = backStackEntry.sharedViewModel(navController = navController),
                idAph = route.idAph,
                vitalSigns = vitalSigns,
                medicine = medicine,
                signature = signature,
                photoTaken = photoTaken == true
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.MedicalHistoryViewRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: AphRoute.MedicalHistoryViewRoute = backStackEntry.toRoute()

            val photoTaken = backStackEntry.savedStateHandle.get<Boolean>(PHOTO_TAKEN)
            backStackEntry.savedStateHandle.remove<Boolean>(PHOTO_TAKEN)

            MedicalHistoryViewScreen(
                modifier = modifier,
                idAph = route.idAph,
                photoTaken = photoTaken == true
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.CameraRoute> { navBackStackEntry ->
            CameraScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.CameraViewRoute> { navBackStackEntry ->
            CameraViewScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.MedicineRoute> {
            MedicineScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.SendEmailRoute>(
            typeMap = mapOf(
                typeOf<String?>() to NavType.StringType
            )
        ) { backStackEntry ->
            val route: AphRoute.SendEmailRoute = backStackEntry.toRoute()

            SendEmailScreen(
                modifier = modifier,
                idAph = route.idAph
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.SignaturePadRoute> {
            SignaturePadScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.VitalSignsRoute> {
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
    navigation<NavGraph.ReportGraph>(startDestination = ReportRoute.AddFindingRoute::class) {
        composable<ReportRoute.AddFindingRoute>(
            typeMap = mapOf(
                typeOf<String>() to NavType.StringType
            )
        ) { navBackStackEntry ->
            val route: ReportRoute.AddFindingRoute = navBackStackEntry.toRoute()

            AddFindingScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                findingId = route.findingId,
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<ReportRoute.ReportCameraRoute> { navBackStackEntry ->
            ReportCameraScreen(
                modifier = modifier,
                viewModel = navBackStackEntry.sharedViewModel(navController = navController)
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<ReportRoute.ImagesConfirmationRoute>(
            typeMap = mapOf(
                typeOf<String>() to NavType.StringType
            )
        ) { navBackStackEntry ->
            val route: ReportRoute.ImagesConfirmationRoute = navBackStackEntry.toRoute()

            ImagesConfirmationScreen(
                modifier = modifier,
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                from = route.from,
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<ReportRoute.AddReportRoleRoute> {
            AddReportRoleScreen(
                modifier = modifier,
                onNavigation = { roleName ->
                    navController.navigate(ReportRoute.AddReportRoute(roleName))
                },
                onCancel = {
                    navController.navigate(NavGraph.MainGraph) {
                        popUpTo(ReportRoute.AddReportRoleRoute) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<ReportRoute.AddReportRoute>(
            typeMap = mapOf(
                typeOf<String>() to NavType.StringType
            )
        ) { navBackStackEntry ->
            val route: ReportRoute.AddReportRoute = navBackStackEntry.toRoute()

            AddReportScreen(
                modifier = modifier,
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
                roleName = route.roleName,
                onNavigation = { navigationModel ->
                    navigationModel.navigate(navController)
                }
            )
        }
    }
}
