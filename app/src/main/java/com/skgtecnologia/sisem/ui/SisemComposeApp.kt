package com.skgtecnologia.sisem.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.skgtecnologia.sisem.ui.deviceauth.DeviceAuthScreen
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.theme.SisemTheme

@Composable
fun ComposeApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NavigationGraph.Auth.route
        ) {
            authGraph(modifier)
        }
    }
}

private fun NavGraphBuilder.authGraph(modifier: Modifier) {
    navigation(
        startDestination = AuthNavigationRoute.DeviceAuth.route,
        route = NavigationGraph.Auth.route
    ) {
        composable(
            route = AuthNavigationRoute.Login.route
        ) {
            LoginScreen(
                modifier = modifier
            )
        }

        composable(
            route = AuthNavigationRoute.DeviceAuth.route
        ) {
            DeviceAuthScreen(
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeAppPreview() {
    SisemTheme {
        ComposeApp()
    }
}
