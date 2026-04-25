package com.skgtecnologia.sisem.ui.inventory

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class InventoryUiState(
    val screenModel: ScreenModel? = null,
    val navigationModel: InventoryNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
