package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.error.mapToUi
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreen
import com.skgtecnologia.sisem.domain.preoperational.usecases.SendPreOperational
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class PreOperationalViewModel @Inject constructor(
    private val getPreOperationalScreen: GetPreOperationalScreen,
    private val sendPreOperational: SendPreOperational,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var job: Job? = null

    private val operationRole: String? = savedStateHandle[NavigationArgument.ROLE]
    var uiState by mutableStateOf(PreOperationalUiState())
        private set

    init {
        Timber.d("Role is $operationRole")
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getPreOperationalScreen.invoke(OperationRole.getRoleByName(operationRole.orEmpty()))
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
            onFindingForm = true
        )
    }

    fun handleShownFindingForm() {
        uiState = uiState.copy(
            onFindingForm = false
        )
    }

    fun sendPreOperational() {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            sendPreOperational.invoke().onSuccess {
                uiState = uiState.copy(
                    isLoading = false
                )
            }.onFailure { throwable ->
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
