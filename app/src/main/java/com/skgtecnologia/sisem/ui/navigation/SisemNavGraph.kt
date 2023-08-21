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
import com.skgtecnologia.sisem.ui.deviceauth.DeviceAuthScreen
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.map.MapScreen
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.preoperational.PreOperationalScreen

@Composable
fun SisemNavGraph(
    isTablet: Boolean
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()

        // FIXME: Receive this model as param / do this on Splash and pass it to Main
//        val startDestination = getStartDestination(navController, )
        
        NavHost(
            navController = navController,
            startDestination = NavigationGraph.Auth.route
        ) {
            authGraph(navController, isTablet, modifier)
            menuGraph(navController, isTablet, modifier)
            mainGraph() // FIXME: Finish this work
        }
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    isTablet: Boolean,
    modifier: Modifier
) {
    navigation(
        startDestination = AuthNavigationRoute.AuthCards.route,
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
            route = "${AuthNavigationRoute.PreOperational.route}/{${NavigationArgument.ROLE}}",
            arguments = listOf(
                navArgument(NavigationArgument.ROLE) {
                    type = NavType.StringType
                }
            )
        ) {
            PreOperationalScreen(
                isTablet = isTablet,
                modifier = modifier
            )
        }

        composable(
            route = AuthNavigationRoute.ChangePassword.route
        ) {
            ChangePasswordScreen(
                isTablet = isTablet,
                modifier = modifier,
                onNavigation = { navController.navigateUp() }, // FIXME: ???
                onCancel = { navController.navigateUp() }
            )
        }
    }
}

@Suppress("UnusedPrivateMember", "LongMethod")
private fun NavGraphBuilder.menuGraph(
    navController: NavHostController,
    isTablet: Boolean,
    modifier: Modifier
) {
    navigation(
        startDestination = MenuNavigationRoute.MenuScreen.route,
        route = NavigationGraph.Menu.route
    ) {
        composable(
            route = MenuNavigationRoute.MenuScreen.route
        ) {
            MenuDrawer(
                onClick = { menuNavigationRoute ->
                    navController.navigate(menuNavigationRoute.route)
                },
                onLogout = {
                    navController.navigate(AuthNavigationRoute.AuthCards.route) {
                        popUpTo(NavigationGraph.Menu.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = MenuNavigationRoute.IncidentScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.InventoryScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.NotificationsScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.DrivingGuideScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.CertificationsScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.NewsScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.HCEUDCScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.ShiftScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.PreoperationalMenuScreen.route
        ) {
            // FIXME: Finish this work
        }

        composable(
            route = MenuNavigationRoute.DeviceAuth.route
        ) {
            navController.navigate(AuthNavigationRoute.DeviceAuth.route)
        }

        composable(
            route = MenuNavigationRoute.SignatureAndFingerprint.route
        ) {
            // FIXME: Finish this work
        }
    }
}

private fun NavGraphBuilder.mainGraph() {
    navigation(
        startDestination = MainNavigationRoute.Home.route,
        route = NavigationGraph.Main.route
    ) {
        composable(
            route = MainNavigationRoute.Home.route
        ) {
            MapScreen()
        }
    }
}
