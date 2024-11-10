package com.skgtecnologia.sisem.ui.navigation

import kotlinx.serialization.Serializable

// region Navigation Graphs
sealed interface NavGraph {
    @Serializable
    data object AuthGraph : NavGraph

    @Serializable
    data object MainGraph : NavGraph

    @Serializable
    data object AphGraph : NavGraph

    @Serializable
    data object ReportGraph : NavGraph
}
// endregion

// region Navigation Routes
sealed interface NavRoute

// region Auth Routes
sealed interface AuthRoute : NavRoute {
    @Serializable
    data object AuthCardsRoute : AuthRoute

    @Serializable
    data object ChangePasswordRoute : AuthRoute

    @Serializable
    data class DeviceAuthRoute(val from: String) : AuthRoute

    @Serializable
    data object ForgotPasswordRoute : AuthRoute

    @Serializable
    data class LoginRoute(val username: String? = null) : AuthRoute

    @Serializable
    data class PreOperationalRoute(val role: String? = null) : AuthRoute
}
// endregion

// region Main Routes
sealed interface MainRoute : NavRoute {
    @Serializable
    data object CertificationsRoute : MainRoute

    @Serializable
    data object DeviceAuthMainRoute : AuthRoute

    @Serializable
    data object DrivingGuideRoute : MainRoute

    @Serializable
    data object HCEUDCRoute : MainRoute

    @Serializable
    data object IncidentsRoute : MainRoute

    @Serializable
    data object InitSignatureRoute : MainRoute

    @Serializable
    data object InventoryRoute : MainRoute

    @Serializable
    data class InventoryViewRoute(val inventoryName: String) : MainRoute

    @Serializable
    data object MapRoute : MainRoute

    @Serializable
    data object NotificationsRoute : MainRoute

    @Serializable
    data object PreoperationalMainRoute : MainRoute

    @Serializable
    data class PreoperationalViewRoute(val role: String) : MainRoute

    @Serializable
    data object PreStretcherRetentionRoute : MainRoute

    @Serializable
    data class SignatureRoute(val document: String) : MainRoute

    @Serializable
    data object ShiftRoute : MainRoute

    @Serializable
    data class StretcherRetentionRoute(val idAph: String) : MainRoute

    @Serializable
    data class StretcherRetentionViewRoute(val idAph: String) : MainRoute
}
// endregion

// region Aph Routes
sealed interface AphRoute : NavRoute {
    @Serializable
    data object CameraRoute : AphRoute

    @Serializable
    data object CameraViewRoute : AphRoute

    @Serializable
    data class MedicalHistoryRoute(val idAph: String) : AphRoute

    @Serializable
    data class MedicalHistoryViewRoute(val idAph: String) : AphRoute

    @Serializable
    data object MedicineRoute : AphRoute

    @Serializable
    data class SendEmailRoute(val idAph: String) : AphRoute

    @Serializable
    data object SignaturePadRoute : AphRoute

    @Serializable
    data object VitalSignsRoute : AphRoute
}
// endregion

// region Report Routes
sealed interface ReportRoute : NavRoute {
    @Serializable
    data class AddFindingRoute(val findingId: String? = null) : ReportRoute

    @Serializable
    data object AddReportRoleRoute : ReportRoute

    @Serializable
    data class AddReportRoute(val roleName: String) : ReportRoute

    @Serializable
    data class ImagesConfirmationRoute(val from: String) : ReportRoute

    @Serializable
    data object ReportCameraRoute : ReportRoute
}
// endregion

// endregion

object NavigationArgument {
    const val MEDICINE = "medicine"
    const val NOVELTY = "novelty"
    const val PHOTO_TAKEN = "photo_taken"
    const val PHOTO_TAKEN_VIEW = "photo_taken_view"
    const val REVERT_FINDING = "revert_finding"
    const val SIGNATURE = "signature"
    const val VITAL_SIGNS = "vital_signs"
}
