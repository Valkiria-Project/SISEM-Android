package com.skgtecnologia.sisem.ui.inventory.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class InventoryViewNavigationModel(
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.popBackStack()
        }
    }
}
