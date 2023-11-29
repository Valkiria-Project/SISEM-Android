package com.skgtecnologia.sisem.ui.inventory.view

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class InventoryViewUiState(
    val screenModel: ScreenModel? = null,
    val validateFields: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val navigationModel: InventoryViewNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
