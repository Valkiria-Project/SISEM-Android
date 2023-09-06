package com.skgtecnologia.sisem.ui.navigation

sealed class NavigationGraph(val route: String) {
    data object Auth : NavigationGraph("auth_graph")
    data object Main : NavigationGraph("main_graph")
    data object Report : NavigationGraph("report_graph")
}

sealed class AuthNavigationRoute(val route: String) {
    data object AuthCards : AuthNavigationRoute("auth_cards_screen")
    data object DeviceAuth : AuthNavigationRoute("device_auth_screen")
    data object Login : AuthNavigationRoute("login_screen")
    data object PreOperational : AuthNavigationRoute("pre_operational_screen")

    // FIXME: This is not part of AuthGraph
    data object ChangePassword : AuthNavigationRoute("change_password_screen")
}

sealed class MainNavigationRoute(val route: String) {
    data object CertificationsScreen : MainNavigationRoute("certifications_screen")
    data object DeviceAuth : MainNavigationRoute("device_auth")
    data object DrivingGuideScreen : MainNavigationRoute("driving_guide_screen")
    data object HCEUDCScreen : MainNavigationRoute("hce_udc_screen")
    data object IncidentScreen : MainNavigationRoute("incident_screen")
    data object InventoryScreen : MainNavigationRoute("inventory_screen")
    data object MainScreen : MainNavigationRoute("menu_screen")
    data object NewsScreen : MainNavigationRoute("news_screen")
    data object NotificationsScreen : MainNavigationRoute("notifications_screen")
    data object PreoperationalMainScreen : MainNavigationRoute("preoperational_menu_screen")
    data object ShiftScreen : MainNavigationRoute("shift_screen")
    data object SignatureAndFingerprint : MainNavigationRoute("signature_and_fingerprint")
}

sealed class ReportNavigationRoute(val route: String) {
    data object Camera : ReportNavigationRoute("camera_screen")
    data object Findings : ReportNavigationRoute("findings_screen")
    data object ImagesConfirmation : ReportNavigationRoute("images_confirmation_screen")
    data object AddReportRole : ReportNavigationRoute("add_report_role_screen")
    data object AddReportScreen : ReportNavigationRoute("add_report_screen")
}

object NavigationArgument {
    const val ROLE = "operation_role"
    const val FROM = "from"
}
