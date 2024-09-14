package com.skgtecnologia.sisem.ui.forgotpassword

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.NavigationModel

data class ForgotPasswordNavigationModel(
    val isSuccess: Boolean = false,
    val isCancel: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            isCancel || isSuccess -> navController.navigateBack()
        }
    }
}
