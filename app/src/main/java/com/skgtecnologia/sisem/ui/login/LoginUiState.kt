package com.skgtecnologia.sisem.ui.login

import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.LoginNavigationModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class LoginUiState(
    val screenModel: ScreenModel? = null,
    val onLogin: Boolean = false,
    val onForgotPassword: Boolean = false,
    val onLoginLink: LoginLink? = null,
    val validateFields: Boolean = false,
    val loginNavigationModel: LoginNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
