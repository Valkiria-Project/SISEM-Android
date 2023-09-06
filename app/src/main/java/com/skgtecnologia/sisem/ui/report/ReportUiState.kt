package com.skgtecnologia.sisem.ui.report

import android.net.Uri
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel

data class ReportUiState(
    val selectedImageUris: List<Uri> = listOf(),
    val navigationModel: ReportNavigationModel? = null,
)
