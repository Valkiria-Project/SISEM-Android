@file:Suppress("TooManyFunctions")

package com.skgtecnologia.sisem.ui.navigation

const val APP_STARTED = "app_started"
const val FINDING = "finding"
const val LOGIN = "login"
const val MAIN = "main"
const val REPORT = "report"

fun getAppStartDestination(model: StartupNavigationModel?): String {
    return if (model == null) {
        NavigationGraph.Auth.route
    } else when {
        model.isAdmin && !model.vehicleCode.isNullOrEmpty() -> NavigationGraph.Main.route
        model.isTurnStarted && !model.requiresPreOperational -> NavigationGraph.Main.route
        else -> NavigationGraph.Auth.route
    }
}

fun getAuthStartDestination(model: StartupNavigationModel?): String = when {
    model?.isWarning == true -> AuthNavigationRoute.ChangePasswordScreen.route
    model?.isAdmin == true -> "${AuthNavigationRoute.DeviceAuthScreen.route}/$APP_STARTED"
    model?.requiresPreOperational == true -> AuthNavigationRoute.PreOperationalScreen.route
    else -> AuthNavigationRoute.AuthCardsScreen.route
}
