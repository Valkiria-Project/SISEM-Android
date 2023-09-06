package com.skgtecnologia.sisem.ui.navigation

sealed class NavigationGraph(val route: String) {
    data object Auth : NavigationGraph("auth_graph")
    data object Main : NavigationGraph("main_graph")
    data object Report : NavigationGraph("report_graph")
}

sealed class AuthNavigationRoute(val route: String) {
    data object AuthCardsScreen : AuthNavigationRoute("auth_cards_screen")
    data object DeviceAuthScreen : AuthNavigationRoute("device_auth_screen")
    data object LoginScreen : AuthNavigationRoute("login_screen")
    data object PreOperationalScreen : AuthNavigationRoute("pre_operational_screen")

    // FIXME: This is not part of AuthGraph
    data object ChangePasswordScreen : AuthNavigationRoute("change_password_screen")
}

sealed class MainNavigationRoute(val route: String) {
    data object CertificationsScreen : MainNavigationRoute("menu_certifications_screen")
    data object DeviceAuthScreen : MainNavigationRoute("menu_device_auth_screen")
    data object DrivingGuideScreen : MainNavigationRoute("menu_driving_guide_screen")
    data object HCEUDCScreen : MainNavigationRoute("menu_hce_udc_screen")
    data object IncidentScreen : MainNavigationRoute("menu_incident_screen")
    data object InventoryScreen : MainNavigationRoute("menu_inventory_screen")
    data object MainScreen : MainNavigationRoute("menu_screen")
    data object AddReportRoleScreen : MainNavigationRoute("menu_add_report_role_screen")
    data object NotificationsScreen : MainNavigationRoute("menu_notifications_screen")
    data object PreoperationalMainScreen : MainNavigationRoute("menu_preoperational_menu_screen")
    data object ShiftScreen : MainNavigationRoute("menu_shift_screen")
    data object SignatureAndFingerprint : MainNavigationRoute("menu_signature_and_fingerprint")
}

sealed class ReportNavigationRoute(val route: String) {
    data object CameraScreen : ReportNavigationRoute("camera_screen")
    data object FindingsScreen : ReportNavigationRoute("findings_screen")
    data object ImagesConfirmationScreen : ReportNavigationRoute("images_confirmation_screen")
    data object AddReportRoleScreen : ReportNavigationRoute("add_report_role_screen")
    data object AddReportScreen : ReportNavigationRoute("add_report_screen")
}

object NavigationArgument {
    const val ROLE = "operation_role"
    const val FROM = "from"
}
