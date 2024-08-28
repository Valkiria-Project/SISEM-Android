package com.skgtecnologia.sisem.ui.authcards.create

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel

data class AuthCardsUiState(
    val screenModel: ScreenModel? = null,
    val reportDetail: ReportsDetailUiModel? = null,
    val chipSection: ChipSectionUiModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
