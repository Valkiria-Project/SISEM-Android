package com.skgtecnologia.sisem.ui.inventory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.inventory.usecases.GetInventoryInitialScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.valkiria.uicomponents.action.UiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getInventoryInitialScreen: GetInventoryInitialScreen,
    private val logoutCurrentUser: LogoutCurrentUser,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(InventoryUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getInventoryInitialScreen.invoke(androidIdProvider.getAndroidId())
                .onSuccess { inventoryScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = inventoryScreenModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorModel = throwable.mapToUi()
                        )
                    }
                }
        }
    }

    fun navigate(identifier: String) {
        uiState = uiState.copy(
            navigationModel = InventoryNavigationModel(identifier = identifier)
        )
    }

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = InventoryNavigationModel(back = true)
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }

    fun handleEvent(uiAction: UiAction) {
        consumeShownError()

        uiAction.handleAuthorizationErrorEvent {
            job?.cancel()
            job = viewModelScope.launch {
                logoutCurrentUser.invoke()
                    .onSuccess { username ->
                        UnauthorizedEventHandler.publishUnauthorizedEvent(username)
                    }.onFailure {
                        UnauthorizedEventHandler.publishUnauthorizedEvent(it.toString())
                    }
            }
        }
    }

    private fun consumeShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
