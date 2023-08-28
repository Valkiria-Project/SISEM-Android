package com.skgtecnologia.sisem.ui.navigation

sealed class NavigationGraph(val route: String) {
    object Auth : NavigationGraph("auth")
    object Common : NavigationGraph("common")
    object Main : NavigationGraph("main")
}

sealed class AuthNavigationRoute(val route: String) {
    object AuthCards : AuthNavigationRoute("auth_cards")
    object Login : AuthNavigationRoute("login")
    object DeviceAuth : AuthNavigationRoute("device_auth")
    object PreOperational : AuthNavigationRoute("pre_operational")

    // FIXME: This is not part of AuthGraph
    object ChangePassword : AuthNavigationRoute("change_password")
}

sealed class CommonNavigationRoute(val route: String) {
    object ImageSelection : CommonNavigationRoute("image_selection")
    object NewsScreen : CommonNavigationRoute("news_screen")
    object RecordNewsScreen : CommonNavigationRoute("record_news_screen")
}

sealed class MainNavigationRoute(val route: String) {
    object MainScreen : MainNavigationRoute("menu_screen")
    object IncidentScreen : MainNavigationRoute("incident_screen")
    object InventoryScreen : MainNavigationRoute("inventory_screen")
    object NotificationsScreen : MainNavigationRoute("notifications_screen")
    object DrivingGuideScreen : MainNavigationRoute("driving_guide_screen")
    object CertificationsScreen : MainNavigationRoute("certifications_screen")
    object NewsScreen : MainNavigationRoute("news_screen")
    object ShiftScreen : MainNavigationRoute("shift_screen")
    object PreoperationalMainScreen : MainNavigationRoute("preoperational_menu_screen")
    object HCEUDCScreen : MainNavigationRoute("hce_udc_screen")
    object DeviceAuth : MainNavigationRoute("device_auth")
    object SignatureAndFingerprint : MainNavigationRoute("signature_and_fingerprint")
}

object NavigationArgument {
    const val ROLE = "operation_role"
}
