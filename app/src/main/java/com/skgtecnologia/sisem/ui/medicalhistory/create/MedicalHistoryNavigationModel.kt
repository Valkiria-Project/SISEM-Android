package com.skgtecnologia.sisem.ui.medicalhistory.create

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.core.navigation.AphRoute
import com.skgtecnologia.sisem.core.navigation.NavigationArgument
import com.skgtecnologia.sisem.core.navigation.NavigationModel

data class MedicalHistoryNavigationModel(
    val isInfoCardEvent: Boolean = false,
    val isMedsSelectorEvent: Boolean = false,
    val isSignatureEvent: Boolean = false,
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val back: Boolean = false
) : NavigationModel {

    override fun navigate(navController: NavHostController) {
        super.navigate(navController)

        when {
            back -> navController.navigateBack()

            isInfoCardEvent -> navController.navigate(AphRoute.VitalSignsRoute)

            isMedsSelectorEvent -> navController.navigate(AphRoute.MedicineRoute)

            isSignatureEvent -> navController.navigate(AphRoute.SignaturePadRoute)

            showCamera -> navController.navigate(AphRoute.CameraRoute)
            photoTaken -> with(navController) {
                navigateBack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.PHOTO_TAKEN, true)
            }
        }
    }
}
