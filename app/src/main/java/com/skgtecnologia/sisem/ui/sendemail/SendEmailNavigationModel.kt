package com.skgtecnologia.sisem.ui.sendemail

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class SendEmailNavigationModel(
    val back: Boolean = false,
    val send: Boolean = false
) : NavigationModel
