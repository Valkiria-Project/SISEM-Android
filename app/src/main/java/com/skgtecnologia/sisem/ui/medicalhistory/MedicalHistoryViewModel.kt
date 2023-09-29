package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    private val getMedicalHistoryScreen: GetMedicalHistoryScreen,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicalHistoryUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getMedicalHistoryScreen.invoke(
                serial = androidIdProvider.getAndroidId(),
                incidentCode = "101",
                patientId = "13"
            ).onSuccess {
                uiState = uiState.copy(
                    isLoading = false, screenModel = it
                )
            }.onFailure { throwable ->
                uiState = uiState.copy(
                    isLoading = false, errorModel = throwable.mapToUi()
                )
            }
        }
    }
}
