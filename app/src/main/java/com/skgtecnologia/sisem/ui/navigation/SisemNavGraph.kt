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
import com.skgtecnologia.sisem.ui.authcards.AuthCardsScreen
import com.skgtecnologia.sisem.ui.changepassword.ChangePasswordScreen
import com.skgtecnologia.sisem.ui.commons.extensions.sharedViewModel
import com.skgtecnologia.sisem.ui.deviceauth.DeviceAuthScreen
import com.skgtecnologia.sisem.ui.forgotpassword.ForgotPasswordScreen
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.map.MapScreen
import com.skgtecnologia.sisem.ui.media.CameraScreen
import com.skgtecnologia.sisem.ui.media.ImagesConfirmationScreen
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import com.skgtecnologia.sisem.ui.preoperational.PreOperationalScreen
import com.skgtecnologia.sisem.ui.report.AddFindingScreen
import com.skgtecnologia.sisem.ui.report.AddReportRoleScreen
import com.skgtecnologia.sisem.ui.report.AddReportScreen

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
        ) {
            PreOperationalScreen(
                modifier = modifier
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

@Suppress("UnusedPrivateMember", "LongMethod")
private fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    navigation(
        startDestination = MainNavigationRoute.MainScreen.route,
        route = NavigationGraph.Main.route
    ) {
        composable(
            route = MainNavigationRoute.MainScreen.route
        ) {
            MapScreen(
                onClick = { menuNavigationRoute ->
                    navController.navigate(menuNavigationRoute.route)
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
            // FIXME: Finish this work
        }

        composable(
            route = MainNavigationRoute.InventoryScreen.route
        ) {
            // FIXME: Finish this work
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
            route = MainNavigationRoute.AddReportRoleScreen.route
        ) {
            navController.navigate(ReportNavigationRoute.AddReportRoleScreen.route)
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
            // FIXME: Finish this work
        }

        composable(
            route = MainNavigationRoute.DeviceAuthScreen.route
        ) {
            navController.navigate("${AuthNavigationRoute.DeviceAuthScreen.route}/$MAIN")
        }

        composable(
            route = MainNavigationRoute.SignatureAndFingerprint.route
        ) {
            // FIXME: Finish this work
        }
    }
}

@Suppress("UnusedPrivateMember", "LongMethod")
private fun NavGraphBuilder.reportGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    navigation(
        startDestination = ReportNavigationRoute.AddFindingScreen.route,
        route = NavigationGraph.Report.route
    ) {
        composable(
            route = "${ReportNavigationRoute.AddFindingScreen.route}/{${NavigationArgument.ROLE}}",
            arguments = listOf(navArgument(NavigationArgument.ROLE) { type = NavType.StringType })
        ) { backStackEntry ->
            AddFindingScreen(
                viewModel = backStackEntry.sharedViewModel(navController = navController),
                role = backStackEntry.arguments?.getString(NavigationArgument.ROLE).orEmpty(),
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = ReportNavigationRoute.CameraScreen.route
        ) {
            CameraScreen(
                viewModel = it.sharedViewModel(navController = navController)
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = ReportNavigationRoute.ImagesConfirmationScreen.route +
                "/{${NavigationArgument.FROM}}",
            arguments = listOf(navArgument(NavigationArgument.FROM) { type = NavType.StringType })
        ) { backStackEntry ->
            ImagesConfirmationScreen(
                viewModel = backStackEntry.sharedViewModel(navController = navController),
                from = backStackEntry.arguments?.getString(NavigationArgument.FROM).orEmpty(),
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
                onNavigation = { role ->
                    navController.navigate("${ReportNavigationRoute.AddReportScreen.route}/$role")
                },
                onCancel = {
                    navController.navigate(NavigationGraph.Main.route) {
                        popUpTo(MainNavigationRoute.AddReportRoleScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = "${ReportNavigationRoute.AddReportScreen.route}/{${NavigationArgument.ROLE}}",
            arguments = listOf(navArgument(NavigationArgument.ROLE) { type = NavType.StringType })
        ) { backStackEntry ->
            AddReportScreen(
                viewModel = backStackEntry.sharedViewModel(navController = navController),
                role = backStackEntry.arguments?.getString(NavigationArgument.ROLE).orEmpty(),
                onNavigation = { navigationModel ->
                    navigateToNextStep(navController, navigationModel)
                }
            )
        }
    }
}
