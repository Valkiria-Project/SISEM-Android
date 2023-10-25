package com.skgtecnologia.sisem.ui.changepassword

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.login.LoginNavigationModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class ChangePasswordUiState(
    val screenModel: ScreenModel? = null,
    val loginNavigationModel: LoginNavigationModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val onCancel: Boolean = false,
    val successInfoModel: BannerUiModel? = null,
    val errorModel: BannerUiModel? = null
)
