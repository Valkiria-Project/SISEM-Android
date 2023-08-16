package com.skgtecnologia.sisem.ui.changepassword

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class ChangePasswordUiState(
    val screenModel: ScreenModel? = null,
    val onChangePassword: Boolean = false,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
