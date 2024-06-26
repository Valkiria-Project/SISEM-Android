package com.skgtecnologia.sisem.ui.deviceauth

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.LOGIN
import com.skgtecnologia.sisem.ui.navigation.MAIN
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class DeviceAuthNavigationModel(
    val isCrewList: Boolean = false,
    val isCancel: Boolean = false,
    val from: String = ""
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            isCrewList && from == LOGIN ->
                navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                    popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                        inclusive = true
                    }
                }

            isCrewList && from == "" ->
                navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                    popUpTo(AuthNavigationRoute.DeviceAuthScreen.route) {
                        inclusive = true
                    }
                }

            isCrewList && from == MAIN ->
                navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                    popUpTo(NavigationGraph.Main.route) {
                        inclusive = true
                    }
                }

            isCancel && from == MAIN ->
                navController.navigate(NavigationGraph.Main.route) {
                    popUpTo(AuthNavigationRoute.DeviceAuthScreen.route) {
                        inclusive = true
                    }
                }
        }
    }
}
