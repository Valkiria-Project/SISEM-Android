package com.skgtecnologia.sisem.ui.medicalhistory.view

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryViewScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MedicalHistoryViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val getMedicalHistoryViewScreen: GetMedicalHistoryViewScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicalHistoryViewUiState())
        private set

    private val idAph: Int? = savedStateHandle[NavigationArgument.ID_APH]

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getMedicalHistoryViewScreen.invoke(idAph = idAph.toString())
                .onSuccess { medicalHistoryViewScreen ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = medicalHistoryViewScreen,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorEvent = throwable.mapToUi()
                        )
                    }
                }
        }
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            isLoading = false,
            navigationModel = null
        )
    }

    fun handleEvent(uiAction: UiAction) {
        consumeShownError()

        uiAction.handleAuthorizationErrorEvent {
            job?.cancel()
            job = viewModelScope.launch {
                logoutCurrentUser.invoke()
                    .onSuccess {
                        UnauthorizedEventHandler.publishUnauthorizedEvent()
                    }
            }
        }
    }

    private fun consumeShownError() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }

    fun navigateBack() {
        uiState = uiState.copy(
            navigationModel = MedicalHistoryViewNavigationModel(back = true)
        )
    }

    fun showCamera() {
        uiState = uiState.copy(
            navigationModel = MedicalHistoryViewNavigationModel(showCamera = true)
        )
    }

    fun updateMediaActions(selectedMedia: List<Uri>? = null) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)

            if (selectedMedia?.isNotEmpty() == true) addAll(selectedMedia)
        }

        val updatedBody = uiState.screenModel?.body?.map { model ->
            if (model is MediaActionsUiModel) {
                model.copy(selectedMediaUris = updatedSelectedMedia)
            } else {
                model
            }
        }.orEmpty()

        uiState = uiState.copy(
            selectedMediaUris = if (selectedMedia == null) {
                uiState.selectedMediaUris
            } else {
                updatedSelectedMedia
            },
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun removeMediaActionsImage(selectedMedia: Uri) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)

            removeIf { uri ->
                selectedMedia.toString() == uri.toString()
            }
        }

        val updatedBody = uiState.screenModel?.body?.map { model ->
            if (model is MediaActionsUiModel) {
                model.copy(selectedMediaUris = updatedSelectedMedia)
            } else {
                model
            }
        }.orEmpty()

        uiState = uiState.copy(
            selectedMediaUris = updatedSelectedMedia,
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun sendMedicalHistoryView() {
        uiState = uiState.copy(
            navigationModel = MedicalHistoryViewNavigationModel(sendMedical = true)
        )
    }
}
