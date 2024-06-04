package com.skgtecnologia.sisem.ui.medicalhistory.create

import androidx.navigation.NavHostController
import com.skgtecnologia.sisem.commons.extensions.navigateBack
import com.skgtecnologia.sisem.ui.navigation.AphNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

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

            isInfoCardEvent -> navController.navigate(AphNavigationRoute.VitalSignsScreen.route)

            isMedsSelectorEvent ->
                navController.navigate(AphNavigationRoute.MedicineScreen.route)

            isSignatureEvent ->
                navController.navigate(AphNavigationRoute.SignaturePadScreen.route)

            showCamera -> navController.navigate(AphNavigationRoute.CameraScreen.route)
            photoTaken -> with(navController) {
                navigateBack()

                currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(NavigationArgument.PHOTO_TAKEN, true)
            }
        }
    }
}
