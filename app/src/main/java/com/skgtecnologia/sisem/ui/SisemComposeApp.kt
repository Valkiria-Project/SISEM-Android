package com.skgtecnologia.sisem.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skgtecnologia.sisem.ui.login.LoginScreen
import com.skgtecnologia.sisem.ui.navigation.NavigationRoute
import com.skgtecnologia.sisem.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun ComposeApp(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.LOGIN
    ) {
        composable(
            route = NavigationRoute.LOGIN
        ) {
            LoginScreen(
                modifier = modifier,
                onClick = {
//                    navController.navigate(NavigationRoute.FETCH)
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
