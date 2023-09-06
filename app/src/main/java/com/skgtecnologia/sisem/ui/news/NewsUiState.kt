package com.skgtecnologia.sisem.ui.news

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.model.ui.errorbanner.ErrorUiModel

data class NewsUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
