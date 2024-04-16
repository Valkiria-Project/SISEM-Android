package com.skgtecnologia.sisem.ui.authcards.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.ROLE
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class AuthCardViewNavigationModel(
    val role: OperationRole? = null,
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.popBackStack()

            role != null -> {
                navController.navigate(
                    MainNavigationRoute.PreOperationalViewScreen.route + "?$ROLE=${role.name}"
                )
            }
        }
    }
}
