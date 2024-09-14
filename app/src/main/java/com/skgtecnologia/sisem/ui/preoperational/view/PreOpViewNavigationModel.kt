package com.skgtecnologia.sisem.ui.preoperational.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.NavigationModel

data class PreOpViewNavigationModel(
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()
        }
    }
}
