package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetLoginNavigationModel
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreen
import com.skgtecnologia.sisem.domain.preoperational.usecases.SendPreOperational
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreOperationalViewModel @Inject constructor(
    private val getLoginNavigationModel: GetLoginNavigationModel,
    private val getPreOperationalScreen: GetPreOperationalScreen,
    private val sendPreOperational: SendPreOperational
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(PreOperationalUiState())
        private set

    var findings = mutableStateMapOf<String, Boolean>()
    var inventoryValues = mutableStateMapOf<String, Int>()
    var fieldsValues = mutableStateMapOf<String, String>()
    var novelties = mutableStateListOf<Novelty>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getPreOperationalScreen.invoke()
                .onSuccess { loginScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = loginScreenModel,
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

    fun showFindingForm() {
        uiState = uiState.copy(
            preOpNavigationModel = PreOpNavigationModel(isNewFinding = true)
        )
    }

    fun handleShownFindingForm() {
        uiState = uiState.copy(
            preOpNavigationModel = null
        )
    }

    fun sendPreOperational() {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            sendPreOperational.invoke(
                findings.toMap(),
                inventoryValues.toMap(),
                fieldsValues.toMap(),
                novelties.toList()
            ).onSuccess {
                getLoginNavigationModel()
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    errorModel = throwable.mapToUi()
                )
            }
        }
    }

    private suspend fun getLoginNavigationModel() {
        getLoginNavigationModel.invoke()
            .onSuccess { loginNavigationModel ->
                uiState = uiState.copy(
                    preOpNavigationModel = PreOpNavigationModel(
                        isTurnComplete = loginNavigationModel.isTurnComplete
                    ),
                    isLoading = false
                )
            }
            .onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    errorModel = throwable.mapToUi()
                )
            }
    }

    fun onPreOpHandled() {
        uiState = uiState.copy(
            preOpNavigationModel = null,
            isLoading = false
        )
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
