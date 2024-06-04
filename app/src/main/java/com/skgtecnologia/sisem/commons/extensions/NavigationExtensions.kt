package com.skgtecnologia.sisem.commons.extensions

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavHostController.navigateBack(): Boolean {
    return if (canGoBack) {
        popBackStack()
    } else {
        false
    }
}
