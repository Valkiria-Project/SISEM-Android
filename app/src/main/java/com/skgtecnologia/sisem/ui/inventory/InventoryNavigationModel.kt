package com.skgtecnologia.sisem.ui.inventory

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class InventoryNavigationModel(
    val back: Boolean = false,
    val identifier: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()
            identifier != null -> navController.navigate(
                MainNavigationRoute.InventoryViewScreen.route +
                    "?${NavigationArgument.INVENTORY_TYPE}=$identifier"
            )
        }
    }
}
