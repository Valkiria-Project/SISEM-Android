package com.skgtecnologia.sisem.ui.media

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(MediaUiState())
        private set

    fun goBack() {
        uiState = uiState.copy(
            onGoBack = true
        )
    }

    fun handleGoBack() {
        uiState = uiState.copy(
            onGoBack = false
        )
    }

    fun updateSelectedImages(selectedImages: List<Uri>) {
        uiState = uiState.copy(
            selectedImageUris = buildList {
                uiState.selectedImageUris.forEach {
                    add(it)
                }

                selectedImages.forEach {
                    add(it)
                }
            }
        )
    }

    fun showCamera() {
        uiState = uiState.copy(
            onShowCamera = true
        )
    }

    fun handleShowCamera() {
        uiState = uiState.copy(
            onShowCamera = false
        )
    }

    fun onPhotoTaken(savedUri: Uri) {
        uiState = uiState.copy(
            selectedImageUris = buildList {
                uiState.selectedImageUris.forEach {
                    add(it)
                }

                add(savedUri)
            },
            onPhotoTaken = true
        )
    }

    fun handleOnPhotoTaken() {
        uiState = uiState.copy(
            onPhotoTaken = false
        )
    }

    fun confirmMedia() {
        uiState = uiState.copy(
            onMediaConfirmed = true
        )
    }

    fun handleConfirmImages() {
        uiState = uiState.copy(
            onMediaConfirmed = false
        )
    }
}
