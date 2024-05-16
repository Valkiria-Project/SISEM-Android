package com.skgtecnologia.sisem.ui.stretcherretention.create

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class StretcherRetentionNavigationModel(
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.popBackStack()
        }
    }
}
