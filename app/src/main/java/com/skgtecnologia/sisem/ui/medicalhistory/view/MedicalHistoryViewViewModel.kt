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
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SaveAphFiles
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.skgtecnologia.sisem.ui.medicalhistory.MedicalHistoryNavigationModel
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.chip.FiltersUiModel
import com.valkiria.uicomponents.components.media.MediaActionsComponent
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MedicalHistoryViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val getMedicalHistoryViewScreen: GetMedicalHistoryViewScreen,
    private val observeOperationConfig: ObserveOperationConfig,
    private val saveAphFiles: SaveAphFiles
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
                            selectedMediaUris = medicalHistoryViewScreen.body
                                .filterIsInstance<MediaActionsUiModel>()
                                .first()
                                .selectedMediaUris,
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

            observeOperationConfig.invoke()
                .onSuccess { operationConfig ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            operationConfig = operationConfig
                        )
                    }
                }.onFailure { throwable ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            errorEvent = throwable.mapToUi()
                        )
                    }
                }
        }
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

    fun showCamera() {
        uiState = uiState.copy(
            navigationModel = MedicalHistoryViewNavigationModel(showCamera = true)
        )
    }

    fun onPhotoTaken(savedUri: Uri) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)
            add(savedUri.toString())
        }

        uiState = uiState.copy(
            selectedMediaUris = updatedSelectedMedia,
            navigationModel = MedicalHistoryViewNavigationModel(
                photoTaken = true
            )
        )
    }

    fun updateMediaActions(selectedMedia: List<String>? = null) {
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

    fun removeMediaActionsImage(selectedMediaIndex: Int) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)

            removeAt(selectedMediaIndex)
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

    fun sendMedicalHistoryView(images: List<File>) {
        if (images.isEmpty()) {
            uiState = uiState.copy(
                isLoading = false,
                navigationModel = MedicalHistoryViewNavigationModel(sendMedical = idAph)
            )
        } else {
            job?.cancel()
            job = viewModelScope.launch {
                saveAphFiles.invoke(
                    idAph = idAph.toString(),
                    images = images.mapIndexed { index, image ->
                        ImageModel(
                            fileName = "Img_$idAph" + "_$index.jpg",
                            file = image
                        )
                    }
                ).onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            navigationModel = MedicalHistoryViewNavigationModel(sendMedical = idAph)
                        )
                    }
                }.onFailure { throwable ->
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
                    .onSuccess { username ->
                        UnauthorizedEventHandler.publishUnauthorizedEvent(username)
                    }
            }
        }
    }

    private fun consumeShownError() {
        uiState = uiState.copy(
            errorEvent = null
        )
    }

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = MedicalHistoryViewNavigationModel(back = true)
        )
    }
}
