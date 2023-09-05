package com.skgtecnologia.sisem.ui.report

import android.net.Uri

data class ReportUiState(
    val selectedImageUris: List<Uri> = listOf(),
    val onGoBack: Boolean = false,
    val onShowCamera: Boolean = false,
    val onPhotoTaken: Boolean = false,
    val onMediaConfirmed: Boolean = false,
    val onMediaDelete: Boolean = false,
    val onSaveFinding: Boolean = false
)
