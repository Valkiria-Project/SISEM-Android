package com.skgtecnologia.sisem.ui.stretcherretention.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.NavigationModel

data class StretcherRetentionViewNavigationModel(
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()
        }
    }
}
