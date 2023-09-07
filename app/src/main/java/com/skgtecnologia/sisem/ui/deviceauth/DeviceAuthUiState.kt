package com.skgtecnologia.sisem.ui.deviceauth

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel

data class DeviceAuthUiState(
    val screenModel: ScreenModel? = null,
    val disassociateInfoModel: BannerUiModel? = null,
    val onDeviceAuthenticated: Boolean = false,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
