package com.skgtecnologia.sisem.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.skgtecnologia.sisem.ui.authcards.AuthCardsScreen
import com.skgtecnologia.sisem.ui.deviceauth.DeviceAuthScreen
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.map.MapScreen
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph

@Composable
fun SisemComposeApp(
    isTablet: Boolean
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NavigationGraph.Auth.route
        ) {
            authGraph(navController, isTablet, modifier)
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
            ) {
                navController.navigate(AuthNavigationRoute.DeviceAuth.route)
            }
        }

        composable(
            route = AuthNavigationRoute.DeviceAuth.route
        ) {
            DeviceAuthScreen(
                isTablet = isTablet,
                modifier = modifier
            )
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
