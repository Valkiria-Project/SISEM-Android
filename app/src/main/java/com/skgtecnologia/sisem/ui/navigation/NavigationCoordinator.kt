@file:Suppress("TooManyFunctions")

package com.skgtecnologia.sisem.ui.navigation

const val APP_STARTED = "app_started"
const val FINDING = "finding"
const val LOGIN = "login"
const val MAIN = "main"
const val REPORT = "report"

fun getAppStartDestination(model: StartupNavigationModel?): NavGraph {
    return if (model == null) {
        NavGraph.AuthGraph
    } else when {
        model.isAdmin && !model.vehicleCode.isNullOrEmpty() -> NavGraph.MainGraph
        model.isTurnStarted && !model.requiresPreOperational -> NavGraph.MainGraph
        else -> NavGraph.AuthGraph
    }
}

fun getAuthStartDestination(model: StartupNavigationModel?): AuthRoute = when {
    model?.isWarning == true -> AuthRoute.ChangePasswordRoute
    model?.isAdmin == true -> AuthRoute.DeviceAuthRoute(APP_STARTED)
    model?.requiresPreOperational == true -> AuthRoute.PreOperationalRoute()
    else -> AuthRoute.AuthCardsRoute
}
