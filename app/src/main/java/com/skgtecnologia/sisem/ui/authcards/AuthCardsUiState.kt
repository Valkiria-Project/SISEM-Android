package com.skgtecnologia.sisem.ui.authcards

import com.valkiria.uicomponents.model.ui.chip.ChipSectionUiModel
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel

data class AuthCardsUiState(
    val screenModel: ScreenModel? = null,
    val reportDetail: ReportsDetailUiModel? = null,
    val chipSection: ChipSectionUiModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
