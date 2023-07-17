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
}

sealed class AuthNavigationRoute(val route: String) {
    object Login : AuthNavigationRoute("login")
    object DeviceAuth : AuthNavigationRoute("device_auth")
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

object NavigationArgument {
    const val TBD = "tbd"
}
