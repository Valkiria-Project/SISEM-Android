package com.skgtecnologia.sisem.ui.incident

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class IncidentUiState(
    val screenModel: ScreenModel? = null,
    val navigationModel: IncidentNavigationModel? = null,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null
)
