package com.skgtecnologia.sisem.ui.navigation

sealed class NavigationGraph(val route: String) {
    data object Auth : NavigationGraph("auth")
    data object Main : NavigationGraph("main")
    data object Media : NavigationGraph("common")
}

sealed class AuthNavigationRoute(val route: String) {
    data object AuthCards : AuthNavigationRoute("auth_cards")
    data object DeviceAuth : AuthNavigationRoute("device_auth")
    data object Login : AuthNavigationRoute("login")
    data object PreOperational : AuthNavigationRoute("pre_operational")

    // FIXME: This is not part of AuthGraph
    data object ChangePassword : AuthNavigationRoute("change_password")
}

sealed class CommonNavigationRoute(val route: String) {
    data object NewsScreen : CommonNavigationRoute("news_screen")
    data object RecordNewsScreen : CommonNavigationRoute("record_news_screen")
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

sealed class MediaNavigationRoute(val route: String) {
    data object Camera : MediaNavigationRoute("camera")
    data object ImageSelection : MediaNavigationRoute("image_selection")
}

object NavigationArgument {
    const val ROLE = "operation_role"
}
