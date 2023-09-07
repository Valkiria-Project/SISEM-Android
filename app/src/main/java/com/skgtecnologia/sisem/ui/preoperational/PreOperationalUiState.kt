package com.skgtecnologia.sisem.ui.preoperational

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel

data class PreOperationalUiState(
    val screenModel: ScreenModel? = null,
    val preOpNavigationModel: PreOpNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
