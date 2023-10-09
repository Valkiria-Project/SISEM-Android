package com.skgtecnologia.sisem.ui.report

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class AddReportRoleUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
