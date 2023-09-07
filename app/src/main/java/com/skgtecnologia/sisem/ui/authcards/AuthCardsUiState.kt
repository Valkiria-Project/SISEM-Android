package com.skgtecnologia.sisem.ui.authcards

import com.skgtecnologia.sisem.domain.model.bricks.ChipSectionModel
import com.skgtecnologia.sisem.domain.model.bricks.ReportsDetailModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel

data class AuthCardsUiState(
    val screenModel: ScreenModel? = null,
    val reportDetail: ReportsDetailModel? = null,
    val chipSection: ChipSectionModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
