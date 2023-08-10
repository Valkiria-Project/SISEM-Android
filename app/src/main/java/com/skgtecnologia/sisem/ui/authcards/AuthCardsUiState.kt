package com.skgtecnologia.sisem.ui.authcards

import com.skgtecnologia.sisem.domain.model.body.ReportsDetailModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class AuthCardsUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val reportDetail: ReportsDetailModel? = null,
    val errorModel: ErrorUiModel? = null
)