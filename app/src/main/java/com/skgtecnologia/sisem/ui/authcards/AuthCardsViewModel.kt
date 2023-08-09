package com.skgtecnologia.sisem.ui.authcards

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.authcards.usecases.Config
import com.skgtecnologia.sisem.domain.authcards.usecases.GetAuthCardsScreen
import com.skgtecnologia.sisem.domain.model.body.ReportsDetailModel
import com.skgtecnologia.sisem.domain.model.error.mapToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class AuthCardsViewModel @Inject constructor(
    private val config: Config,
    private val getAuthCardsScreen: GetAuthCardsScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(AuthCardsUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            config.invoke()
                .onSuccess {
                    getAuthCardsScreen()
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

    fun showBottomSheet(reportDetail: ReportsDetailModel) {
        uiState = uiState.copy(
            reportDetail = reportDetail
        )
    }

    fun handleShownBottomSheet() {
        uiState = uiState.copy(
            reportDetail = null
        )
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }

    private suspend fun getAuthCardsScreen() {
        getAuthCardsScreen.invoke()
            .onSuccess { authCardsScreenModel ->
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        screenModel = authCardsScreenModel,
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
