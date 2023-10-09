package com.skgtecnologia.sisem.ui.login

import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class LoginUiState(
    val screenModel: ScreenModel? = null,
    val onLoginLink: LoginLink? = null,
    val validateFields: Boolean = false,
    val navigationModel: LoginNavigationModel? = null,
    val isLoading: Boolean = false,
    val warning: BannerUiModel? = null,
    val errorModel: BannerUiModel? = null
)
