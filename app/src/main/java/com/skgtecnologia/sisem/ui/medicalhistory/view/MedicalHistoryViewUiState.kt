package com.skgtecnologia.sisem.ui.medicalhistory.view

import android.net.Uri
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class MedicalHistoryViewUiState(
    val screenModel: ScreenModel? = null,
    val selectedMediaUris: List<Uri> = listOf(),
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val errorEvent: BannerUiModel? = null,
    val navigationModel: MedicalHistoryViewNavigationModel? = null
)
