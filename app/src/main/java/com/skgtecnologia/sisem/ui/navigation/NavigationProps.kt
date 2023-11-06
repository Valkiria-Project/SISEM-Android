package com.skgtecnologia.sisem.ui.navigation

sealed class NavigationGraph(val route: String) {
    data object Auth : NavigationGraph("auth_graph")
    data object Main : NavigationGraph("main_graph")
    data object Report : NavigationGraph("report_graph")
}

sealed class NavigationRoute(open val route: String)

sealed class AuthNavigationRoute(override val route: String) : NavigationRoute(route) {
    data object AuthCardsScreen : AuthNavigationRoute("auth_cards_screen")
    data object LoginScreen : AuthNavigationRoute("login_screen")
    data object ForgotPasswordScreen : AuthNavigationRoute("forgot_password_screen")
    data object PreOperationalScreen : AuthNavigationRoute("pre_operational_screen")
    data object DeviceAuthScreen : AuthNavigationRoute("device_auth_screen")
    data object ChangePasswordScreen : AuthNavigationRoute("change_password_screen")
}

sealed class MainNavigationRoute(override val route: String) : NavigationRoute(route) {
    data object CameraScreen : MainNavigationRoute("camera_screen")
    data object CertificationsScreen : MainNavigationRoute("menu_certifications_screen")
    data object DeviceAuthScreen : MainNavigationRoute("menu_device_auth_screen")
    data object DrivingGuideScreen : MainNavigationRoute("menu_driving_guide_screen")
    data object HCEUDCScreen : MainNavigationRoute("menu_hce_udc_screen")
    data object IncidentScreen : MainNavigationRoute("menu_incident_screen")
    data object InventoryScreen : MainNavigationRoute("menu_inventory_screen")
    data object MainScreen : MainNavigationRoute("main_screen")
    data object MedicineScreen : MainNavigationRoute("medicine_screen")
    data object NotificationsScreen : MainNavigationRoute("menu_notifications_screen")
    data object PreoperationalMainScreen : MainNavigationRoute("menu_preoperational_menu_screen")
    data object ShiftScreen : MainNavigationRoute("menu_shift_screen")
    data object SignatureAndFingerprint : MainNavigationRoute("menu_signature_and_fingerprint")
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
    const val FINDING_ID = "finding_id"
    const val FROM = "from"
    const val MEDICINE = "medicine"
    const val NOVELTY = "novelty"
    const val REVERT_FINDING = "revert_finding"
    const val SIGNATURE = "signature"
    const val VITAL_SIGNS = "vital_signs"
}
