package com.skgtecnologia.sisem.ui.medicalhistory

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.model.MedicalHistoryNavigationModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class MedicalHistoryUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null,
    val navigationModel: MedicalHistoryNavigationModel? = null
)