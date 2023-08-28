package com.skgtecnologia.sisem.ui.recordnews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.model.error.mapToUi
import com.skgtecnologia.sisem.domain.recordnews.usecases.GetRecordNewsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class RecordNewsViewModel @Inject constructor(
    private val getRecordNewsScreen: GetRecordNewsScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(RecordNewsUiState())
        private set

    var topic by mutableStateOf("")
    var description by mutableStateOf("")
    var isValidTopic by mutableStateOf(false)
    var isValidDescription by mutableStateOf(false)

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getRecordNewsScreen.invoke()
                .onSuccess { recordNewsScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = recordNewsScreenModel,
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

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
