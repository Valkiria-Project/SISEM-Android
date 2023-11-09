package com.skgtecnologia.sisem.ui.inventory

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class InventoryNavigationModel(
    val back: Boolean = false,
    val identifier: String? = null
) : NavigationModel
