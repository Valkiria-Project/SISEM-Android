package com.skgtecnologia.sisem.ui.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MapUiState())
        private set

    init {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {

        }
    }
}
