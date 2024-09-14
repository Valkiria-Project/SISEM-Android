package com.skgtecnologia.sisem.ui.stretcherretention.pre

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.core.navigation.NavigationModel

data class PreStretcherRetentionNavigationModel(
    val back: Boolean = false,
    val patientAph: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()

            patientAph != null -> navController.navigate(
                MainRoute.StretcherRetentionRoute(patientAph)
            )
        }
    }
}
