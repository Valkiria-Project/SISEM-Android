package com.skgtecnologia.sisem.ui.navigation

import android.os.Parcelable
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// region Graphs
sealed interface NavGraph {
    @Serializable
    data object AuthNavGraph : NavGraph

    @Serializable
    data object AphNavGraph : NavGraph

    @Serializable
    data object MainNavGraph : NavGraph

    @Serializable
    data object ReportNavGraph : NavGraph
}
// endregion

sealed class NavigationRoute(open val route: String)

// region Auth Routes
sealed interface AuthRoute {
    @Serializable
    data object AuthCardsRoute : AuthRoute

    @Serializable
    data class LoginRoute(val username: String? = null) : AuthRoute

    @Serializable
    data object ForgotPasswordRoute : AuthRoute

    @Serializable
    data class PreOperationalRoute(
        val role: OperationRole,
        val revertFinding: Boolean? = null,
        val novelty: Novelty? = null
    ) : AuthRoute

    @Serializable
    data class DeviceAuthRoute(
        val deviceAppState: String
    ) : AuthRoute

    @Serializable
    data object ChangePasswordRoute : AuthRoute
}
// endregion

sealed class MainNavigationRoute(override val route: String) : NavigationRoute(route) {
    data object CertificationsScreen : MainNavigationRoute("menu_certifications_screen")
    data object DeviceAuthScreen : MainNavigationRoute("menu_device_auth_screen")
    data object DrivingGuideScreen : MainNavigationRoute("menu_driving_guide_screen")
    data object HCEUDCScreen : MainNavigationRoute("menu_hce_udc_screen")
    data object IncidentScreen : MainNavigationRoute("menu_incident_screen")
    data object InventoryScreen : MainNavigationRoute("menu_inventory_screen")
    data object InventoryViewScreen : MainNavigationRoute("menu_inventory_view_screen")
    data object MapScreen : MainNavigationRoute("map_screen")
    data object NotificationsScreen : MainNavigationRoute("menu_notifications_screen")
    data object PreoperationalMainScreen : MainNavigationRoute("menu_preoperational_menu_screen")
    data object PreOperationalViewScreen : MainNavigationRoute("pre_operational_view_screen")
    data object ShiftScreen : MainNavigationRoute("menu_shift_screen")
    data object InitSignatureScreen : MainNavigationRoute("menu_init_signature_screen")
    data object SignatureScreen : MainNavigationRoute("signature_screen")
    data object PreStretcherRetentionScreen : MainNavigationRoute("pre_stretcher_retention_screen")
    data object StretcherRetentionScreen : MainNavigationRoute("stretcher_retention_screen")
    data object StretcherViewScreen : MainNavigationRoute("stretcher_view_screen")
}

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

sealed class ReportNavigationRoute(override val route: String) : NavigationRoute(route) {
    data object ReportCameraScreen : ReportNavigationRoute("report_camera_screen")
    data object AddFindingScreen : ReportNavigationRoute("add_finding_screen")
    data object ImagesConfirmationScreen : ReportNavigationRoute("images_confirmation_screen")
    data object AddReportRoleScreen : ReportNavigationRoute("add_report_role_screen")
    data object AddReportScreen : ReportNavigationRoute("add_report_screen")
}

object NavigationArgument {
    const val DOCUMENT = "document"
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
    const val USERNAME = "username"
    const val VITAL_SIGNS = "vital_signs"
}
