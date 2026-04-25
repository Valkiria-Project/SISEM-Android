package com.skgtecnologia.sisem.ui.medicalhistory.view

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.AphRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class MedicalHistoryViewNavigationModel(
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val sendMedical: String? = null,
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()

            sendMedical != null -> navController.navigate(AphRoute.SendEmailRoute(sendMedical))

            showCamera -> navController.navigate(AphRoute.CameraViewRoute)
            photoTaken -> with(navController) {
                navigateBack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.PHOTO_TAKEN_VIEW, true)
            }
        }
    }
}
