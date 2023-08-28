package com.skgtecnologia.sisem.ui.imageselection

data class ImageSelectionUiState(
    val onGoBack: Boolean = false,
    val onTakePicture: Boolean = false,
    val onImagesConfirmed: Boolean = false,
    val onImagesCancelled: Boolean = false
)
