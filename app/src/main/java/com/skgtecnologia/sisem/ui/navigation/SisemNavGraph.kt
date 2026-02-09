package com.skgtecnologia.sisem.ui.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.compose.rememberFragmentState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.PHOTO_TAKEN_VIEW
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

@Composable
fun SisemNavGraph(navigationModel: StartupNavigationModel?) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()
        val context = LocalContext.current
        val startDestination = getAppStartDestination(navigationModel)

        LaunchedEffect(Unit) {
            if (startDestination == NavGraph.MainGraph) {
                Intent(context.applicationContext, LocationService::class.java).apply {
                    action = ACTION_START
                    context.startService(this)
                }
            }
        }

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
            startDestination = startDestination
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

        composable<AuthRoute.LoginRoute> {
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

        composable<AuthRoute.DeviceAuthRoute> {
            DeviceAuthScreen(
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

        composable<AuthRoute.PreOperationalRoute> { backStackEntry ->
            // TECH-DEBT
            val revertFinding = backStackEntry.savedStateHandle.get<Boolean>(REVERT_FINDING)
            backStackEntry.savedStateHandle.remove<Boolean>(REVERT_FINDING)

            // TECH-DEBT
            val novelty = backStackEntry.savedStateHandle.get<Novelty>(NOVELTY)
            backStackEntry.savedStateHandle.remove<Novelty>(NOVELTY)

            PreOperationalScreen(
                modifier = modifier,
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
    navigation<NavGraph.MainGraph>(startDestination = MainRoute.MapRoute::class) {
        composable<MainRoute.MapRoute> {
            val fragmentState = rememberFragmentState()

            MapScreen(
                modifier = modifier,
                fragmentState = fragmentState,
                onMenuAction = { navRoute -> navController.navigate(route = navRoute) },
                onAction = { aphRoute -> navController.navigate(route = aphRoute) },
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

        composable<MainRoute.InventoryViewRoute> {
            InventoryViewScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.NotificationsRoute> { /*TECH-DEBT: Finish this work*/ }

        composable<MainRoute.DrivingGuideRoute> { /*TECH-DEBT: Finish this work*/ }

        composable<MainRoute.CertificationsRoute> { /*TECH-DEBT: Finish this work*/ }

        composable<MainRoute.HCEUDCRoute> { /*TECH-DEBT: Finish this work*/ }

        composable<MainRoute.ShiftRoute> { /*TECH-DEBT: Finish this work*/ }

        composable<MainRoute.PreoperationalMainRoute> {
            AuthCardViewScreen { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.DeviceAuthMainRoute> {
            DeviceAuthScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.InitSignatureRoute> {
            InitSignatureScreen(modifier = modifier) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.SignatureRoute> { backStackEntry ->
            // TECH-DEBT
            val signature = backStackEntry.savedStateHandle.get<String>(SIGNATURE)
            backStackEntry.savedStateHandle.remove<String>(SIGNATURE)

            SignatureScreen(
                modifier = modifier,
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

        composable<MainRoute.StretcherRetentionRoute> {
            StretcherRetentionScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.StretcherRetentionViewRoute> {
            StretcherRetentionViewScreen(
                modifier = modifier
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<MainRoute.PreoperationalViewRoute> {
            PreOperationalViewScreen(
                modifier = modifier
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
    navigation<NavGraph.AphGraph>(startDestination = AphRoute.MedicalHistoryRoute::class) {
        composable<AphRoute.MedicalHistoryRoute> { backStackEntry ->
            // TECH-DEBT
            val vitalSigns = backStackEntry.savedStateHandle.get<Map<String, String>>(VITAL_SIGNS)
            backStackEntry.savedStateHandle.remove<Map<String, String>>(VITAL_SIGNS)

            // TECH-DEBT
            val medicine = backStackEntry.savedStateHandle.get<Map<String, String>>(MEDICINE)
            backStackEntry.savedStateHandle.remove<Map<String, String>>(MEDICINE)

            // TECH-DEBT
            val signature = backStackEntry.savedStateHandle.get<String>(SIGNATURE)
            backStackEntry.savedStateHandle.remove<String>(SIGNATURE)

            // TECH-DEBT
            val photoTaken = backStackEntry.savedStateHandle.get<Boolean>(PHOTO_TAKEN)
            backStackEntry.savedStateHandle.remove<Boolean>(PHOTO_TAKEN)

            MedicalHistoryScreen(
                modifier = modifier,
                viewModel = backStackEntry.sharedViewModel(navController = navController),
                vitalSigns = vitalSigns,
                medicine = medicine,
                signature = signature,
                photoTaken = photoTaken == true
            ) { navigationModel ->
                navigationModel.navigate(navController)
            }
        }

        composable<AphRoute.MedicalHistoryViewRoute> { backStackEntry ->
            // TECH-DEBT
            val photoTaken = backStackEntry.savedStateHandle.get<Boolean>(PHOTO_TAKEN_VIEW)
            backStackEntry.savedStateHandle.remove<Boolean>(PHOTO_TAKEN_VIEW)

            MedicalHistoryViewScreen(
                modifier = modifier,
                viewModel = backStackEntry.sharedViewModel(navController = navController),
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

        composable<AphRoute.SendEmailRoute> {
            SendEmailScreen(
                modifier = modifier
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
        composable<ReportRoute.AddFindingRoute> { navBackStackEntry ->
            AddFindingScreen(
                viewModel = navBackStackEntry.sharedViewModel(navController = navController),
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

        composable<ReportRoute.ImagesConfirmationRoute> { navBackStackEntry ->
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

        composable<ReportRoute.AddReportRoute> { navBackStackEntry ->
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
