package com.skgtecnologia.sisem.ui.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.skgtecnologia.sisem.ui.camera.CameraUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private var job: Job? = null

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
