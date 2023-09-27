package com.skgtecnologia.sisem.ui.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.skgtecnologia.sisem.ui.commons.extensions.locationFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    var uiState by mutableStateOf(MapUiState())
        private set

    // FIXME: This should be observed
    fun getLocationCoordinates() {
        fusedLocationClient.locationFlow()
            .conflate()
            .catch { e ->
                Timber.d("Unable to get location $e")
            }
            .onEach { location ->
                Timber.d("Location: $location")
                uiState = uiState.copy(location = location.longitude to location.latitude)
            }
            .launchIn(viewModelScope)
    }
}