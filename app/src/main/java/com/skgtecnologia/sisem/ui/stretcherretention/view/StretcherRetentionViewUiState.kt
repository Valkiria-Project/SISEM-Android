package com.skgtecnologia.sisem.ui.stretcherretention.view

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class StretcherRetentionViewUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val navigationModel: StretcherRetentionViewNavigationModel? = null
)
