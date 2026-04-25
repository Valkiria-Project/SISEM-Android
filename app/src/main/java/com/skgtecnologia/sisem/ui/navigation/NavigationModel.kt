package com.skgtecnologia.sisem.ui.navigation

import androidx.navigation.NavHostController
import timber.log.Timber

interface NavigationModel {

    fun navigate(navController: NavHostController): Unit =
        Timber.d("Navigate with: $this")
}
