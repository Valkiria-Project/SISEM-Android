package com.skgtecnologia.sisem.ui.deviceauth

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.model.DeviceAuthNavigationModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class DeviceAuthUiState(
    val screenModel: ScreenModel? = null,
    val disassociateInfoModel: BannerUiModel? = null,
    val validateFields: Boolean = false,
    val navigationModel: DeviceAuthNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
