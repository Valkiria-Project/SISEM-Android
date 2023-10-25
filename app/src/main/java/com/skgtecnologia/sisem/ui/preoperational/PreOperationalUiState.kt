package com.skgtecnologia.sisem.ui.preoperational

import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class PreOperationalUiState(
    val screenModel: ScreenModel? = null,
    val operationModel: OperationModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val navigationModel: PreOpNavigationModel? = null
)
