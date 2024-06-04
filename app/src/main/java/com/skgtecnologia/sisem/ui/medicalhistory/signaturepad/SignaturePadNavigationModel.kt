package com.skgtecnologia.sisem.ui.medicalhistory.signaturepad

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class SignaturePadNavigationModel(
    val goBack: Boolean = false,
    val signature: String? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            goBack -> navController.navigateBack()

            signature != null -> with(navController) {
                navigateBack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.SIGNATURE, signature)
            }
        }
    }
}
