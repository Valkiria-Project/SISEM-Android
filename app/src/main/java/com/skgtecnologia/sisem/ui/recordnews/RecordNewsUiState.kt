package com.skgtecnologia.sisem.ui.recordnews

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class RecordNewsUiState(
    val screenModel: ScreenModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
