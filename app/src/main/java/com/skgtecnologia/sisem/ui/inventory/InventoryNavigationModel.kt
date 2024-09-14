package com.skgtecnologia.sisem.ui.inventory

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.core.navigation.NavigationModel

data class InventoryNavigationModel(
    val back: Boolean = false,
    val identifier: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()
            identifier != null -> navController.navigate(MainRoute.InventoryViewRoute(identifier))
        }
    }
}
