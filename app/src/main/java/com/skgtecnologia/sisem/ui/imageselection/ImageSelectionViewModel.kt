package com.skgtecnologia.sisem.ui.imageselection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageSelectionViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(ImageSelectionUiState())
        private set

    fun takePicture() {
        uiState = uiState.copy(
            onTakePicture = true
        )
    }

    fun handleTakePicture() {
        uiState = uiState.copy(
            onTakePicture = false
        )
    }

    fun confirmImages() {
        uiState = uiState.copy(
            onImagesConfirmed = true
        )
    }

    fun handleConfirmImages() {
        uiState = uiState.copy(
            onImagesConfirmed = false
        )
    }
}
