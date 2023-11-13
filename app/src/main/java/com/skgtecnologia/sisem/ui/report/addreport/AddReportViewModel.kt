package com.skgtecnologia.sisem.ui.report.addreport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.report.usecases.GetAddReportScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddReportViewModel @Inject constructor(
    private val getAddReportScreen: GetAddReportScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(AddReportUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getAddReportScreen.invoke()
                .onSuccess { addReportScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = addReportScreenModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = throwable.mapToUi()
                    )
                }
        }
    }

    fun consumeErrorEvent() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
