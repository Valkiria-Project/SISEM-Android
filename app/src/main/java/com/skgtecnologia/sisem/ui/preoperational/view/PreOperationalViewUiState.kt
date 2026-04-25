package com.skgtecnologia.sisem.ui.preoperational.view

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel

data class PreOperationalViewUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val findingDetail: FindingsDetailUiModel? = null,
    val errorModel: BannerUiModel? = null,
    val navigationModel: PreOpViewNavigationModel? = null
)
