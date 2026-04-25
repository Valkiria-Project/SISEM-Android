package com.skgtecnologia.sisem.ui.forgotpassword

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class ForgotPasswordUiState(
    val screenModel: ScreenModel? = null,
    val successBanner: BannerUiModel? = null,
    val validateFields: Boolean = false,
    val navigationModel: ForgotPasswordNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
