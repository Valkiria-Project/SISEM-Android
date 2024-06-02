package com.skgtecnologia.sisem.ui.medicalhistory.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.DeleteAphFile
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryViewScreen
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SaveAphFiles
import com.skgtecnologia.sisem.domain.model.banner.fileSizeErrorBanner
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.chip.FiltersUiModel
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import com.valkiria.uicomponents.components.media.MediaItemUiModel
import com.valkiria.uicomponents.components.media.mapToMediaItems
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
    private val deleteAphFile: DeleteAphFile,
    private val getMedicalHistoryViewScreen: GetMedicalHistoryViewScreen,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val observeOperationConfig: ObserveOperationConfig,
    private val saveAphFiles: SaveAphFiles
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicalHistoryViewUiState())
        private set

    private val idAph: Int? = savedStateHandle[NavigationArgument.ID_APH]

    private var mediaFiles = mutableStateListOf<String>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getMedicalHistoryViewScreen.invoke(idAph = idAph.toString())
                .onSuccess { medicalHistoryViewScreen ->
                    medicalHistoryViewScreen.getFormInitialValues()

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = medicalHistoryViewScreen,
                            selectedMediaItems = medicalHistoryViewScreen.body
                                .filterIsInstance<MediaActionsUiModel>()
                                .first()
                                .selectedMediaUris
                                .mapToMediaItems(),
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

    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is MediaActionsUiModel -> mediaFiles.addAll(bodyRowModel.selectedMediaUris)
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

    fun onPhotoTaken(mediaItemUiModel: MediaItemUiModel) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaItems)

            if (mediaItemUiModel.isSizeValid) add(mediaItemUiModel)
        }

        val invalidMediaSelected = !mediaItemUiModel.isSizeValid

        uiState = uiState.copy(
            selectedMediaItems = updatedSelectedMedia,
            navigationModel = MedicalHistoryViewNavigationModel(
                photoTaken = true
            ),
            errorEvent = if (invalidMediaSelected) {
                fileSizeErrorBanner().mapToUi()
            } else {
                null
            }
        )
    }

    fun updateMediaActions(mediaItems: List<MediaItemUiModel>? = null) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaItems)

            mediaItems?.forEach { if (it.isSizeValid) add(it) }
        }

        val invalidMediaSelected = mediaItems?.filter { !it.isSizeValid }

        val updatedBody = uiState.screenModel?.body?.map { model ->
            if (model is MediaActionsUiModel) {
                model.copy(selectedMediaUris = updatedSelectedMedia.map { it.name })
            } else {
                model
            }
        }.orEmpty()

        uiState = uiState.copy(
            selectedMediaItems = if (mediaItems == null) {
                uiState.selectedMediaItems
            } else {
                updatedSelectedMedia
            },
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            ),
            errorEvent = if (invalidMediaSelected?.isNotEmpty() == true) {
                fileSizeErrorBanner().mapToUi()
            } else {
                null
            }
        )
    }

    fun removeMediaActionsImage(selectedMediaIndex: Int) {
        deleteAphFile(selectedMediaIndex)
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaItems)

            removeAt(selectedMediaIndex)
        }

        val updatedBody = uiState.screenModel?.body?.map { model ->
            if (model is MediaActionsUiModel) {
                model.copy(selectedMediaUris = updatedSelectedMedia.map { it.name })
            } else {
                model
            }
        }.orEmpty()

        uiState = uiState.copy(
            selectedMediaItems = updatedSelectedMedia,
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    private fun deleteAphFile(selectedMediaIndex: Int) {
        if (selectedMediaIndex in mediaFiles.indices) {
            val file = mediaFiles[selectedMediaIndex]
            mediaFiles.removeAt(selectedMediaIndex)

            job?.cancel()
            job = viewModelScope.launch {
                deleteAphFile.invoke(idAph = idAph.toString(), fileName = file)
            }
        }
    }

    fun sendMedicalHistoryView(images: List<File>, description: String?) {
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
                    },
                    description = description
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
                    }.onFailure {
                        UnauthorizedEventHandler.publishUnauthorizedEvent(it.toString())
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
