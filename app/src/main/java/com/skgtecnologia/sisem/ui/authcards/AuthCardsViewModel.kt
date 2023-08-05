package com.skgtecnologia.sisem.ui.authcards

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.authcards.usecases.Config
import com.skgtecnologia.sisem.domain.authcards.usecases.GetAuthCardsScreen
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
                    getAuthCardsScreen.invoke(code = "1", turnId = "1")
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
                                isLoading = false
                                // errorModel = throwable.mapToUi()
                            )
                        }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false
                        // errorModel = throwable.mapToUi()
                    ) }

        }
    }
}
