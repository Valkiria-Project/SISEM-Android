package com.skgtecnologia.sisem.ui.preoperational.create

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.skgtecnologia.sisem.ui.navigation.NavGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.ReportRoute

data class PreOpNavigationModel(
    val isTurnComplete: Boolean = false,
    val isNewFinding: Boolean = false,
    val findingId: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            isTurnComplete -> navController.navigate(NavGraph.MainGraph) {
                popUpTo(AuthRoute.AuthCardsRoute) {
                    inclusive = true
                }
            }

            isNewFinding && findingId != null ->
                navController.navigate(ReportRoute.AddFindingRoute(findingId))

            else -> navController.navigate(AuthRoute.AuthCardsRoute) {
                popUpTo(AuthRoute.PreOperationalRoute()) {
                    inclusive = true
                }
            }
        }
    }
}
