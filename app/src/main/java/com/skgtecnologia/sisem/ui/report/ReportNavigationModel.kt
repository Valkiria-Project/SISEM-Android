package com.skgtecnologia.sisem.ui.report

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.AuthRoute
import com.skgtecnologia.sisem.core.navigation.FINDING
import com.skgtecnologia.sisem.core.navigation.NavGraph
import com.skgtecnologia.sisem.core.navigation.NavigationArgument
import com.skgtecnologia.sisem.core.navigation.NavigationModel
import com.skgtecnologia.sisem.core.navigation.REPORT
import com.skgtecnologia.sisem.core.navigation.ReportRoute
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty

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
