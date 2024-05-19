package com.skgtecnologia.sisem.ui.medicalhistory.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.ui.navigation.AphNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class MedicalHistoryViewNavigationModel(
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val sendMedical: Int? = null,
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.popBackStack()

            sendMedical != null -> navController.navigate(
                AphNavigationRoute.SendEmailScreen.route + "/$sendMedical"
            )

            showCamera -> navController.navigate(AphNavigationRoute.CameraViewScreen.route)
            photoTaken -> with(navController) {
                popBackStack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.PHOTO_TAKEN, true)
            }
        }
    }
}
