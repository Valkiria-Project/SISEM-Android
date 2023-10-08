package com.skgtecnologia.sisem.ui.report

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.banner.BannerUiModel

data class AddReportUiState(
    val screenModel: ScreenModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
