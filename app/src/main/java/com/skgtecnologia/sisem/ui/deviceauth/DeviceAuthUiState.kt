package com.skgtecnologia.sisem.ui.deviceauth

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

data class DeviceAuthUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false
)
