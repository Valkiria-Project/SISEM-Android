package com.skgtecnologia.sisem.ui.preoperational.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreenView
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel
import com.valkiria.uicomponents.components.chip.FiltersUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreOperationalViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val androidIdProvider: AndroidIdProvider,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val getPreOperationalScreenView: GetPreOperationalScreenView
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(PreOperationalViewUiState())
        private set

    private val role = savedStateHandle.toRoute<MainRoute.PreoperationalViewRoute>().role

    init {
        val role = OperationRole.getRoleByName(role)

        if (role != null) {
            uiState = uiState.copy(isLoading = true)

            job?.cancel()
            job = viewModelScope.launch {
                getPreOperationalScreenView.invoke(androidIdProvider.getAndroidId(), role)
                    .onSuccess { preOperationalScreenModel ->
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                screenModel = preOperationalScreenModel,
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
        } else {
            goBack()
        }
    }

    fun showFindings(findingDetail: FindingsDetailUiModel?) {
        uiState = uiState.copy(
            findingDetail = findingDetail
        )
    }

    fun handleShownFindingBottomSheet() {
        uiState = uiState.copy(
            findingDetail = null
        )
    }

    fun handleFiltersAction(filtersAction: GenericUiAction.FiltersAction) {
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            updater = { model ->
                if (model is FiltersUiModel) {
                    model.copy(selected = filtersAction.identifier)
                } else {
                    model
                }
            }
        )

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = PreOpViewNavigationModel(back = true)
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
            job = viewModelScope.launch(Dispatchers.IO) {
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
