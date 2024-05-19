package com.skgtecnologia.sisem.ui.incident

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AphNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class IncidentNavigationModel(
    val back: Boolean = false,
    val stretcherRetentionAph: String? = null,
    val patientAph: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.popBackStack()
            patientAph != null -> navController.navigate(
                AphNavigationRoute.MedicalHistoryViewScreen.route + "/$patientAph"
            )

            stretcherRetentionAph != null -> navController.navigate(
                MainNavigationRoute.StretcherViewScreen.route + "/$stretcherRetentionAph"
            )
        }
    }
}
