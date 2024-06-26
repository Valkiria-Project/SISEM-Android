package com.skgtecnologia.sisem.ui.medicalhistory.view

import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.components.media.MediaItemUiModel

data class MedicalHistoryViewUiState(
    val screenModel: ScreenModel? = null,
    val operationConfig: OperationModel? = null,
    val selectedMediaItems: List<MediaItemUiModel> = listOf(),
    val description: String? = null,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val errorEvent: BannerUiModel? = null,
    val navigationModel: MedicalHistoryViewNavigationModel? = null
)
