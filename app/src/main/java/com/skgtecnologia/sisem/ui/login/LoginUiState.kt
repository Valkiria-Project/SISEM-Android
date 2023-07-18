package com.skgtecnologia.sisem.ui.login

import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class LoginUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val bottomSheetLink: LoginLink? = null,
    val errorModel: ErrorUiModel? = null
)
