package com.skgtecnologia.sisem.ui.report

import android.net.Uri
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class ReportUiState(
    val selectedImageUris: List<Uri> = listOf(),
    val operationConfig: OperationModel? = null,
    val successInfoModel: BannerUiModel? = null,
    val cancelInfoModel: BannerUiModel? = null,
    val confirmInfoModel: BannerUiModel? = null,
    val validateFields: Boolean = false,
    val isLoading: Boolean = false,
    val infoEvent: BannerUiModel? = null,
    val navigationModel: ReportNavigationModel? = null
)
