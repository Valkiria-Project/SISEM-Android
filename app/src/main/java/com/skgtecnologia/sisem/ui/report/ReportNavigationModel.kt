package com.skgtecnologia.sisem.ui.report

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.FINDING
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.REPORT
import com.skgtecnologia.sisem.ui.navigation.ReportNavigationRoute

data class ReportNavigationModel(
    val goBackFromReport: Boolean = false,
    val goBackFromImages: Boolean = false,
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val cancelFinding: Boolean = false,
    val closeFinding: Boolean = false,
    val cancelReport: Boolean = false,
    val closeReport: Boolean = false,
    val imagesSize: Int = 0,
    val novelty: Novelty? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            goBackFromReport -> with(navController) {
                popBackStack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.REVERT_FINDING, true)
            }

            goBackFromImages -> navController.popBackStack()
            showCamera -> navController.navigate(ReportNavigationRoute.ReportCameraScreen.route)
            photoTaken -> navController.popBackStack()
            closeFinding && imagesSize > 0 -> navController.navigate(
                "${ReportNavigationRoute.ImagesConfirmationScreen.route}/$FINDING"
            )

            closeFinding -> with(navController) {
                popBackStack(
                    route = AuthNavigationRoute.PreOperationalScreen.route,
                    inclusive = false
                )

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.NOVELTY, novelty)
            }

            closeReport && imagesSize > 0 -> navController.navigate(
                "${ReportNavigationRoute.ImagesConfirmationScreen.route}/$REPORT"
            )

            closeReport ->
                navController.navigate(NavigationGraph.Main.route) {
                    popUpTo(ReportNavigationRoute.AddReportRoleScreen.route) {
                        inclusive = true
                    }
                }
        }
    }
}
