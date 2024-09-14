package com.skgtecnologia.sisem.ui.incident

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.AphRoute
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.core.navigation.NavigationModel

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
                AphRoute.MedicalHistoryViewRoute(patientAph)
            )

            stretcherRetentionAph != null -> navController.navigate(
                MainRoute.StretcherRetentionViewRoute(stretcherRetentionAph)
            )
        }
    }
}
