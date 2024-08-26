package com.skgtecnologia.sisem.ui.deviceauth

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
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

            isCrewList && from == MAIN ->
                navController.navigate(AuthRoute.AuthCardsRoute) {
                    popUpTo(NavigationGraph.Main.route) {
                        inclusive = true
                    }
                }

            isCancel && from == MAIN ->
                navController.navigate(NavigationGraph.Main.route) {
                    popUpTo(AuthRoute.DeviceAuthRoute) {
                        inclusive = true
                    }
                }
        }
    }
}
