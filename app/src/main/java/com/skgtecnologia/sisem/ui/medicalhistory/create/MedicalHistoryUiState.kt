package com.skgtecnologia.sisem.ui.medicalhistory.create

import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.medicalhistory.create.MedicalHistoryNavigationModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.components.media.MediaItemUiModel

data class MedicalHistoryUiState(
    val screenModel: ScreenModel? = null,
    val operationConfig: OperationModel? = null,
    val selectedMediaItems: List<MediaItemUiModel> = listOf(),
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val errorEvent: BannerUiModel? = null,
    val navigationModel: MedicalHistoryNavigationModel? = null
)
