package com.skgtecnologia.sisem.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.valkiria.uicomponents.theme.MyApplicationTheme

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
            authGraph(navController, modifier)
        }
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController, modifier: Modifier) {
    navigation(
            startDestination = AuthNavigationRoute.Login.route,
            route = NavigationGraph.Auth.route
    ) {
        composable(
                route = AuthNavigationRoute.Login.route
        ) {
            LoginScreen(
                    modifier = modifier
            ) {
//                    navController.navigate(NavigationRoute.FETCH)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeAppPreview() {
    MyApplicationTheme {
        ComposeApp()
    }
}
