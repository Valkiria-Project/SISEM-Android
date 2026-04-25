package com.skgtecnologia.sisem.ui.stretcherretention.create

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class StretcherRetentionUiState(
    val screenModel: ScreenModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val successEvent: BannerUiModel? = null,
    val infoEvent: BannerUiModel? = null,
    val navigationModel: StretcherRetentionNavigationModel? = null
)
