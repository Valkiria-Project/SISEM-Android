package com.skgtecnologia.sisem.ui.media

import android.net.Uri

data class MediaUiState(
    val selectedImageUris: List<Uri> = listOf(),
    val onGoBack: Boolean = false,
    val onShowCamera: Boolean = false,
    val onPhotoTaken: Boolean = false,
    val onMediaConfirmed: Boolean = false,
    val onMediaDelete: Boolean = false
)
