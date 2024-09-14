package com.skgtecnologia.sisem.core.navigation

import androidx.navigation.NavHostController
import timber.log.Timber

interface NavigationModel {

    fun navigate(navController: NavHostController): Unit =
        Timber.d("Navigate with: $this")
}
