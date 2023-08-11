package com.skgtecnologia.sisem.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Send
import androidx.compose.ui.graphics.vector.ImageVector
import com.skgtecnologia.sisem.R

sealed class NavigationGraph(val route: String) {
    object Auth : NavigationGraph("auth")
    object Main : NavigationGraph("main")
    object Menu : NavigationGraph("menu")
}

sealed class AuthNavigationRoute(val route: String) {
    object AuthCards : AuthNavigationRoute("auth_cards")
    object Login : AuthNavigationRoute("login")
    object DeviceAuth : AuthNavigationRoute("device_auth")
    object PreOperational : AuthNavigationRoute("pre_operational")
}

sealed class MainNavigationRoute(
    val route: String,
    @StringRes val labelId: Int,
    val icon: ImageVector
) {
    object Home : MainNavigationRoute("home", R.string.app_name, Icons.Outlined.Home)
    object Send : MainNavigationRoute("send", R.string.app_name, Icons.Outlined.Send)
    object Deposit : MainNavigationRoute("deposit", R.string.app_name, Icons.Outlined.Download)
    object Withdraw : MainNavigationRoute("withdraw", R.string.app_name, Icons.Filled.Upload)
}

sealed class MenuNavigationRoute(val route: String) {
    object MenuScreen : MenuNavigationRoute("menu_screen")
    object IncidentScreen : MenuNavigationRoute("incident_screen")
    object InventoryScreen : MenuNavigationRoute("inventory_screen")
    object NotificationsScreen : MenuNavigationRoute("notifications_screen")
    object DrivingGuideScreen : MenuNavigationRoute("driving_guide_screen")
    object CertificationsScreen : MenuNavigationRoute("certifications_screen")
    object NewsScreen : MenuNavigationRoute("news_screen")
    object ShiftScreen : MenuNavigationRoute("shift_screen")
    object PreoperationalMenuScreen : MenuNavigationRoute("preoperational_menu_screen")
    object HCEUDCScreen : MenuNavigationRoute("hce_udc_screen")
}

object NavigationArgument {
    const val TBD = "tbd"
}
