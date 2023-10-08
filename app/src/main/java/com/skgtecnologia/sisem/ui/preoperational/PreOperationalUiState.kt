package com.skgtecnologia.sisem.ui.preoperational

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.valkiria.uicomponents.components.banner.BannerUiModel

data class PreOperationalUiState(
    val screenModel: ScreenModel? = null,
    val validateFields: Boolean = false,
    val navigationModel: PreOpNavigationModel? = null,
    val isLoading: Boolean = false,
    val infoModel: BannerUiModel? = null
)
