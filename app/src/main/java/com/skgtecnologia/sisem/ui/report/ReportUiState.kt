package com.skgtecnologia.sisem.ui.report

import android.net.Uri
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
import com.valkiria.uicomponents.model.ui.errorbanner.ErrorUiModel

data class ReportUiState(
    val selectedImageUris: List<Uri> = listOf(),
    val operationModel: OperationModel? = null,
    val successInfoModel: ErrorUiModel? = null,
    val validateFields: Boolean = false,
    val navigationModel: ReportNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorModel: ErrorUiModel? = null
)
