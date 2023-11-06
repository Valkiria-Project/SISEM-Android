package com.skgtecnologia.sisem.ui.preoperational

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class PreoperationalViewUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null,
    val navigationModel: PreOpViewNavigationModel? = null
)
