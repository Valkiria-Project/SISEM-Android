package com.skgtecnologia.sisem.ui.medicalhistory.medicine

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class MedicineUiState(
    val screenModel: ScreenModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val navigationModel: MedicineNavigationModel? = null
)
