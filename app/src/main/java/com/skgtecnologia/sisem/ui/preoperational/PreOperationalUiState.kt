package com.skgtecnologia.sisem.ui.preoperational

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

data class PreOperationalUiState(
    val screenModel: ScreenModel? = null,
    val onFindingForm: Boolean = false,
    val preOpNavigationModel: PreOpNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
