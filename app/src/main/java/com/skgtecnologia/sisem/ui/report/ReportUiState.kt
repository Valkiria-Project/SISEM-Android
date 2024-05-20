package com.skgtecnologia.sisem.ui.report

import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.components.media.MediaItemUiModel

data class ReportUiState(
    val roleName: String? = null,
    val selectedMediaItems: List<MediaItemUiModel> = listOf(),
    val operationConfig: OperationModel? = null,
    val successInfoModel: BannerUiModel? = null,
    val cancelInfoModel: BannerUiModel? = null,
    val confirmInfoModel: BannerUiModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val navigationModel: ReportNavigationModel? = null
)
