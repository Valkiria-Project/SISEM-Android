package com.skgtecnologia.sisem.ui.preoperational.create

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.ReportNavigationRoute

data class PreOpNavigationModel(
    val isTurnCompleteEvent: Boolean = false,
    val isNewFindingEvent: Boolean = false,
    val findingId: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            isTurnCompleteEvent -> navController.navigate(NavigationGraph.Main.route) {
                popUpTo(AuthNavigationRoute.AuthCardsScreen.route) {
                    inclusive = true
                }
            }

            isNewFindingEvent ->
                navController.navigate(
                    ReportNavigationRoute.AddFindingScreen.route +
                        "?${NavigationArgument.FINDING_ID}=${findingId}"
                )

            else -> navController.navigate(AuthNavigationRoute.AuthCardsScreen.route) {
                popUpTo(AuthNavigationRoute.PreOperationalScreen.route) {
                    inclusive = true
                }
            }
        }
    }
}
