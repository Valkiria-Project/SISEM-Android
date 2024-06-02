package com.skgtecnologia.sisem.ui.medicalhistory.vitalsings

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class VitalSignsNavigationModel(
    val goBack: Boolean = false,
    val values: Map<String, String>? = null
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            goBack -> with(navController) {
                navigateBack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.VITAL_SIGNS, null)
            }

            values != null -> with(navController) {
                navigateBack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.VITAL_SIGNS, values)
            }
        }
    }
}
