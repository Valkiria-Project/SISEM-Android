package com.skgtecnologia.sisem.ui.incident

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.AphNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class IncidentNavigationModel(
    val back: Boolean = false,
    val stretcherRetentionAph: String? = null,
    val patientAph: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()
            patientAph != null -> navController.navigate(
                AphNavigationRoute.MedicalHistoryViewScreen.route + "/$patientAph"
            )

            stretcherRetentionAph != null -> navController.navigate(
                MainRoute.StretcherRetentionViewRoute(stretcherRetentionAph)
            )
        }
    }
}
