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

sealed class NavigationGraph(val route: String) {
    data object Aph : NavigationGraph("aph_graph")
    data object Report : NavigationGraph("report_graph")
}
// endregion

// region Navigation Routes
sealed interface NavRoute
sealed class NavigationRoute(open val route: String)

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

// region Report Routes
sealed interface ReportRoute : NavRoute {
    @Serializable
    data object AddReportRoleRoute : ReportRoute
}

sealed class ReportNavigationRoute(override val route: String) : NavigationRoute(route) {
    data object ReportCameraScreen : ReportNavigationRoute("report_camera_screen")
    data object AddFindingScreen : ReportNavigationRoute("add_finding_screen")
    data object ImagesConfirmationScreen : ReportNavigationRoute("images_confirmation_screen")
    data object AddReportScreen : ReportNavigationRoute("add_report_screen")
}

// endregion
// endregion

sealed class MainNavigationRoute(override val route: String) : NavigationRoute(route)
sealed class AphNavigationRoute(override val route: String) : NavigationRoute(route) {
    data object CameraScreen : MainNavigationRoute("camera_screen")
    data object CameraViewScreen : MainNavigationRoute("camera_view_screen")
    data object MedicalHistoryScreen : MainNavigationRoute("medical_history_screen")
    data object MedicalHistoryViewScreen : MainNavigationRoute("medical_history_view_screen")
    data object MedicineScreen : MainNavigationRoute("medicine_screen")
    data object SendEmailScreen : MainNavigationRoute("send_email_screen")
    data object SignaturePadScreen : MainNavigationRoute("signature_pad_screen")
    data object VitalSignsScreen : MainNavigationRoute("vital_signs_screen")
}

object NavigationArgument {
    const val FINDING_ID = "finding_id"
    const val FROM = "from"
    const val ID_APH = "id_aph"
    const val INVENTORY_TYPE = "inventory_type"
    const val MEDICINE = "medicine"
    const val NOVELTY = "novelty"
    const val PHOTO_TAKEN = "photo_taken"
    const val REVERT_FINDING = "revert_finding"
    const val ROLE = "role"
    const val SIGNATURE = "signature"
    const val VITAL_SIGNS = "vital_signs"
}
