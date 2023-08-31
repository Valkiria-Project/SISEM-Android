package com.skgtecnologia.sisem.ui.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(CameraUiState())
        private set

    fun onPhotoAdded() {
        uiState = uiState.copy(
            onPhotoAdded = true
        )
    }

    fun handleOnPhotoAdded() {
        uiState = uiState.copy(
            onPhotoAdded = false
        )
    }
}
