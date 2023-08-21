package com.skgtecnologia.sisem.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.splash.usecases.GetStartupState
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getStartupState: GetStartupState
) : ViewModel() {

    private var job: Job? = null

    var uiState = MutableStateFlow(StartupUiState())
        private set

    init {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getStartupState.invoke()
                .onSuccess { navigationModel ->
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            StartupUiState(
                                startupNavigationModel = navigationModel
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState.update {
                        StartupUiState(
                            startupNavigationModel = StartupNavigationModel()
                        )
                    }
                }
        }
    }
}
