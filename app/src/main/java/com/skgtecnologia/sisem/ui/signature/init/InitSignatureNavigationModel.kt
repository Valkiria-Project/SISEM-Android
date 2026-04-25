package com.skgtecnologia.sisem.ui.signature.init

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class InitSignatureNavigationModel(
    val back: Boolean = false,
    val document: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()
            document?.isNotEmpty() == true -> navController.navigate(
                MainRoute.SignatureRoute(document)
            )
        }
    }
}
