package com.skgtecnologia.sisem.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.skgtecnologia.sisem.ui.myscreen.fetch.FetchScreen
import com.skgtecnologia.sisem.ui.myscreen.result.ResultScreen
import com.skgtecnologia.sisem.ui.splash.SplashScreen
import com.skgtecnologia.sisem.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ComposeApp(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.SPLASH
    ) {
        composable(
                route = NavigationRoute.SPLASH
        ) {
            SplashScreen(
                    modifier = modifier,
                    onClick = {
                        navController.navigate(NavigationRoute.FETCH)
                    }
            )
        }
        composable(
            route = NavigationRoute.FETCH
        ) {
            FetchScreen(
                modifier = modifier,
                onClick = { identifier ->
                    navController.navigate("${NavigationRoute.MY_SCREEN_UI}/$identifier")
                }
            )
        }
        composable(
            route = "${NavigationRoute.MY_SCREEN_UI}/{${NavigationArgument.ID}}",
            arguments = listOf(
                navArgument(NavigationArgument.ID) {
                    type = NavType.LongType
                }
            )
        ) {
            ResultScreen(
                modifier = modifier,
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Button clicked"
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeAppPreview(
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    MyApplicationTheme {
        ComposeApp(
            scope,
            snackbarHostState
        )
    }
}
