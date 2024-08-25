package com.skgtecnologia.sisem.ui.deviceauth

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.skgtecnologia.sisem.ui.navigation.DeviceAppState
import com.skgtecnologia.sisem.ui.navigation.NavGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class DeviceAuthNavigationModel(
    val isCrewList: Boolean = false,
    val isCancel: Boolean = false,
    val from: String = ""
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            isCrewList && from == DeviceAppState.LOGIN.name ->
                navController.navigate(AuthRoute.AuthCardsRoute) {
                    popUpTo(AuthRoute.AuthCardsRoute) {
                        inclusive = true
                    }
                }

            isCrewList && from == "" ->
                navController.navigate(AuthRoute.AuthCardsRoute) {
                    popUpTo(AuthRoute.DeviceAuthRoute) {
                        inclusive = true
                    }
                }

            isCrewList && from == DeviceAppState.MAIN.name ->
                navController.navigate(AuthRoute.AuthCardsRoute) {
                    popUpTo(NavGraph.MainNavGraph) {
                        inclusive = true
                    }
                }

            isCancel && from == DeviceAppState.MAIN.name ->
                navController.navigate(NavGraph.MainNavGraph) {
                    popUpTo(AuthRoute.DeviceAuthRoute) {
                        inclusive = true
                    }
                }
        }
    }
}
