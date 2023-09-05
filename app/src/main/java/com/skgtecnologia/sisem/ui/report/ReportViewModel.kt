package com.skgtecnologia.sisem.ui.report

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class ReportViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(ReportUiState())
        private set

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                goBack = true
            )
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
            navigationModel = ReportNavigationModel(
                showCamera = true
            )
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
            navigationModel = ReportNavigationModel(
                photoTaken = true
            )
        )
    }

    fun confirmMedia() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                confirmMedia = true
            )
        )
    }

    fun saveFinding() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                saveFinding = true,
                imagesSize = uiState.selectedImageUris.size
            )
        )
    }

    fun handleNavigation() {
        uiState = uiState.copy(
            navigationModel = null
        )
    }
}
