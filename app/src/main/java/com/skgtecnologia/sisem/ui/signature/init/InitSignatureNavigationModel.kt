package com.skgtecnologia.sisem.ui.signature.init

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class InitSignatureNavigationModel(
    val back: Boolean = false,
    val document: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.popBackStack()
            document?.isNotEmpty() == true -> navController.navigate(
                MainNavigationRoute.SignatureScreen.route +
                    "?${NavigationArgument.DOCUMENT}=$document"
            )
        }
    }
}
