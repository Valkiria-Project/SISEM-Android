package com.skgtecnologia.sisem.ui.menu

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel
import com.valkiria.uicomponents.components.banner.BannerUiModel

data class MenuUiState(
    val accessTokenModelList: List<AccessTokenModel>? = null,
    val vehicleConfig: VehicleConfigModel? = null,
    val isLogout: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
