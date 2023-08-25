package com.skgtecnologia.sisem.ui.changepassword

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class ChangePasswordUiState(
    val screenModel: ScreenModel? = null,
    val loginNavigationModel: LoginNavigationModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val onCancel: Boolean = false,
    val successInfoModel: ErrorUiModel? = null,
    val errorModel: ErrorUiModel? = null
)
