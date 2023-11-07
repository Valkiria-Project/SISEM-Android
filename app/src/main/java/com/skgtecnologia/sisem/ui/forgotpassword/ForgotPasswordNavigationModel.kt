package com.skgtecnologia.sisem.ui.forgotpassword

import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class ForgotPasswordNavigationModel(
    val isSuccess: Boolean = false,
    val isCancel: Boolean = false
) : NavigationModel
