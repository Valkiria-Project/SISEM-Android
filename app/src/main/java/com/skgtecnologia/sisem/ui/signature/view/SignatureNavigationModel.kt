package com.skgtecnologia.sisem.ui.signature.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.AphNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class SignatureNavigationModel(
    val isSignatureEvent: Boolean = false,
    val back: Boolean = false,
    val isSaved: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()

            isSignatureEvent ->
                navController.navigate(AphNavigationRoute.SignaturePadScreen.route)

            // FIXME: validar si no se devuelve a la pantalla de firma
            isSaved -> navController.navigate(NavigationGraph.Main.route)
        }
    }
}
