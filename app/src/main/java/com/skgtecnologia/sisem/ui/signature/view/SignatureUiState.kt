package com.skgtecnologia.sisem.ui.signature.view

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class SignatureUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val errorModel: BannerUiModel? = null,
    val navigationModel: SignatureNavigationModel? = null
)
