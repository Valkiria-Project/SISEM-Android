package com.skgtecnologia.sisem.ui.login

import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class LoginUiState(
    val screenModel: ScreenModel? = null,
    val onLogin: Boolean = false,
    val onForgotPassword: Boolean = false,
    val bottomSheetLink: LoginLink? = null,
    val isTurnComplete: Boolean = false,
    val isLoading: Boolean = false,
    val validateFields: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
