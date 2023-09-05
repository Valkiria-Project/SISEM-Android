package com.skgtecnologia.sisem.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import com.skgtecnologia.sisem.ui.news.NewsScreen
import com.skgtecnologia.sisem.ui.preoperational.PreOperationalScreen
import com.skgtecnologia.sisem.ui.media.CameraScreen
import com.skgtecnologia.sisem.ui.report.FindingsScreen
import com.skgtecnologia.sisem.ui.media.ImagesConfirmationScreen
import com.skgtecnologia.sisem.ui.recordnews.RecordNewsScreen

@Composable
fun SisemNavGraph(
    navigationModel: StartupNavigationModel?,
    isTablet: Boolean
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = getAppStartDestination(navigationModel)
        ) {
            authGraph(navController, getAuthStartDestination(navigationModel), isTablet, modifier)
            mainGraph(navController, isTablet, modifier)
            reportGraph(navController, isTablet, modifier)
        }
    }
}

@Suppress("LongMethod")
private fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    startDestination: String,
    isTablet: Boolean,
    modifier: Modifier
) {
    navigation(
        startDestination = startDestination,
        route = NavigationGraph.Auth.route
    ) {
        composable(
            route = AuthNavigationRoute.AuthCards.route
        ) {
            AuthCardsScreen(
                isTablet = isTablet,
                modifier = modifier
            ) {
                navController.navigate(AuthNavigationRoute.Login.route)
            }
        }

        composable(
            route = AuthNavigationRoute.Login.route
        ) {
            LoginScreen(
                isTablet = isTablet,
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = AuthNavigationRoute.DeviceAuth.route
        ) {
            DeviceAuthScreen(
                isTablet = isTablet,
                modifier = modifier,
                onDeviceAuthenticated = {
                    navController.navigate(AuthNavigationRoute.AuthCards.route)
                },
                onCancel = { navController.navigateUp() }
            )
        }

        composable(
            route = AuthNavigationRoute.PreOperational.route
        ) {
            PreOperationalScreen(
                isTablet = isTablet,
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        // FIXME: This is not part of AuthGraph
        composable(
            route = AuthNavigationRoute.ChangePassword.route
        ) {
            ChangePasswordScreen(
                isTablet = isTablet,
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
    isTablet: Boolean,
    modifier: Modifier
) {
    navigation(
        startDestination = MainNavigationRoute.MainScreen.route,
        route = NavigationGraph.Main.route
    ) {
        composable(
            route = MainNavigationRoute.MainScreen.route
        ) {
            MenuDrawer(
                onClick = { menuNavigationRoute ->
                    navController.navigate(menuNavigationRoute.route)
                },
                onLogout = {
                    navController.navigate(AuthNavigationRoute.AuthCards.route) {
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
            route = MainNavigationRoute.NewsScreen.route
        ) {
            navController.navigate(CommonNavigationRoute.NewsScreen.route)
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
            route = MainNavigationRoute.DeviceAuth.route
        ) {
            navController.navigate(AuthNavigationRoute.DeviceAuth.route)
        }

        composable(
            route = MainNavigationRoute.SignatureAndFingerprint.route
        ) {
            // FIXME: Finish this work
        }
    }
}

@Suppress("UnusedPrivateMember")
private fun NavGraphBuilder.reportGraph(
    navController: NavHostController,
    isTablet: Boolean,
    modifier: Modifier
) {
    navigation(
        startDestination = ReportNavigationRoute.Findings.route,
        route = NavigationGraph.Report.route
    ) {
        composable(
            route = ReportNavigationRoute.Findings.route
        ) {
            FindingsScreen(
                viewModel = it.sharedViewModel(navController = navController),
                isTablet = isTablet,
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = ReportNavigationRoute.Camera.route
        ) {
            CameraScreen(
                viewModel = it.sharedViewModel(navController = navController)
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = ReportNavigationRoute.ImagesConfirmation.route
        ) {
            ImagesConfirmationScreen(
                viewModel = it.sharedViewModel(navController = navController),
                isTablet = isTablet,
                modifier = modifier
            ) { navigationModel ->
                navigateToNextStep(navController, navigationModel)
            }
        }

        composable(
            route = CommonNavigationRoute.NewsScreen.route
        ) {
            NewsScreen(
                isTablet = isTablet,
                modifier = modifier,
                onNavigation = { role ->
                    navController.navigate("${CommonNavigationRoute.RecordNewsScreen.route}/$role")
                },
                onCancel = { navController.navigateUp() }
            )
        }

        composable(
            route = "${CommonNavigationRoute.RecordNewsScreen.route}/{${NavigationArgument.ROLE}}",
            arguments = listOf(navArgument(NavigationArgument.ROLE) { type = NavType.StringType })
        ) { backStackEntry ->
            RecordNewsScreen(
                isTablet = isTablet,
                role = backStackEntry.arguments?.getString(NavigationArgument.ROLE).orEmpty(),
                onNavigation = {
                    // FIXME: Finish this work
                },
                onCancel = { navController.navigateUp() }
            )
        }
    }
}
