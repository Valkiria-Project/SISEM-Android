package com.skgtecnologia.sisem.ui.report

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.model.ui.errorbanner.ErrorUiModel

data class AddReportUiState(
    val screenModel: ScreenModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
