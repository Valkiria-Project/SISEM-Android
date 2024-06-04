package com.skgtecnologia.sisem.ui.sendemail

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class SendEmailNavigationModel(
    val back: Boolean = false,
    val send: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()
            send -> navController.navigate(NavigationGraph.Main.route)
        }
    }
}
