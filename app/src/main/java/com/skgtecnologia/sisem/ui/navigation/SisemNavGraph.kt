package com.skgtecnologia.sisem.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.skgtecnologia.sisem.ui.authcards.AuthCardsScreen
import com.skgtecnologia.sisem.ui.changepassword.ChangePasswordScreen
import com.skgtecnologia.sisem.ui.deviceauth.DeviceAuthScreen
import com.skgtecnologia.sisem.ui.imageselection.CameraScreen
import com.skgtecnologia.sisem.ui.imageselection.ImageSelectionScreen
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import com.skgtecnologia.sisem.ui.preoperational.PreOperationalScreen

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
            commonGraph(navController, isTablet, modifier)
            mainGraph(navController, isTablet, modifier)
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

@Suppress("UnusedPrivateMember")
private fun NavGraphBuilder.commonGraph(
    navController: NavHostController,
    isTablet: Boolean,
    modifier: Modifier
) {
    composable(
        route = CommonNavigationRoute.Camera.route
    ) {
        CameraScreen { navigationModel ->
            navigateToNextStep(navController, navigationModel)
        }
    }

    composable(
        route = CommonNavigationRoute.ImageSelection.route
    ) {
        ImageSelectionScreen(
            isTablet = isTablet,
            modifier = modifier
        ) { navigationModel ->
            navigateToNextStep(navController, navigationModel)
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
