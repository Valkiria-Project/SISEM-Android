package com.skgtecnologia.sisem.ui.preoperational.create

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.ReportNavigationRoute

data class PreOpNavigationModel(
    val isTurnComplete: Boolean = false,
    val isNewFinding: Boolean = false,
    val findingId: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            isTurnComplete -> navController.navigate(NavigationGraph.Main.route) {
                popUpTo(AuthRoute.AuthCardsRoute) {
                    inclusive = true
                }
            }

            isNewFinding ->
                navController.navigate(
                    ReportNavigationRoute.AddFindingScreen.route +
                        "?${NavigationArgument.FINDING_ID}=$findingId"
                )

            else -> navController.navigate(AuthRoute.AuthCardsRoute) {
                popUpTo(AuthRoute.PreOperationalRoute()) {
                    inclusive = true
                }
            }
        }
    }
}
