package com.skgtecnologia.sisem.ui.stretcherretention.pre

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class PreStretcherRetentionNavigationModel(
    val back: Boolean = false,
    val patientAph: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.popBackStack()

            patientAph != null -> navController.navigate(
                MainNavigationRoute.StretcherRetentionScreen.route + "/$patientAph"
            )
        }
    }
}
