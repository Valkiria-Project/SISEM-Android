package com.skgtecnologia.sisem.ui.medicalhistory.signaturepad

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class SignaturePadNavigationModel(
    val goBack: Boolean = false,
    val signature: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            goBack -> navController.popBackStack()

            signature != null -> with(navController) {
                popBackStack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.SIGNATURE, signature)
            }
        }
    }
}
