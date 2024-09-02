package com.skgtecnologia.sisem.ui.report

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.skgtecnologia.sisem.ui.navigation.FINDING
import com.skgtecnologia.sisem.ui.navigation.NavGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.navigation.REPORT
import com.skgtecnologia.sisem.ui.navigation.ReportRoute

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
                navigateBack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.REVERT_FINDING, true)
            }

            goBackFromImages -> navController.navigateBack()
            showCamera -> navController.navigate(ReportRoute.ReportCameraRoute)
            photoTaken -> navController.navigateBack()
            closeFinding && imagesSize > 0 -> navController.navigate(
                ReportRoute.ImagesConfirmationRoute(FINDING)
            )

            closeFinding -> with(navController) {
                popBackStack(
                    route = AuthRoute.PreOperationalRoute(),
                    inclusive = false
                )

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.NOVELTY, novelty)
            }

            closeReport && imagesSize > 0 -> navController.navigate(
                ReportRoute.ImagesConfirmationRoute(REPORT)
            )

            closeReport ->
                navController.navigate(NavGraph.MainGraph) {
                    popUpTo(ReportRoute.AddReportRoleRoute) {
                        inclusive = true
                    }
                }
        }
    }
}
