package com.skgtecnologia.sisem.ui.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.banner.fileSizeErrorBanner
import com.skgtecnologia.sisem.domain.model.banner.findingCancellationBanner
import com.skgtecnologia.sisem.domain.model.banner.findingConfirmationBanner
import com.skgtecnologia.sisem.domain.model.banner.findingSavedBanner
import com.skgtecnologia.sisem.domain.model.banner.imagesLimitErrorBanner
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.reportCancellationBanner
import com.skgtecnologia.sisem.domain.model.banner.reportConfirmationBanner
import com.skgtecnologia.sisem.domain.model.banner.reportSentBanner
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.skgtecnologia.sisem.domain.report.usecases.SendReport
import com.skgtecnologia.sisem.ui.navigation.ReportRoute
import com.valkiria.uicomponents.components.media.MediaItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class ReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeOperationConfig: ObserveOperationConfig,
    private val sendReport: SendReport
) : ViewModel() {

    private var job: Job? = null

    var uiState: MutableStateFlow<ReportUiState> = MutableStateFlow(ReportUiState())
        private set

    private val findingId = savedStateHandle.toRoute<ReportRoute.AddFindingRoute>().findingId

    var topic by mutableStateOf("")
    var description by mutableStateOf("")
    var isValidTopic by mutableStateOf(false)
    var isValidDescription by mutableStateOf(false)
    var currentImage by mutableIntStateOf(0)

    init {
        uiState.update { it.copy(isLoading = true) }

        job?.cancel()
        job = viewModelScope.launch {
            observeOperationConfig.invoke()
                .onSuccess { operationModel ->
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                operationConfig = operationModel,
                                isLoading = false
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            infoEvent = throwable.mapToUi()
                        )
                    }
                }
        }
    }

    fun navigateBackFromReport() {
        uiState.update {
            it.copy(
                navigationModel = ReportNavigationModel(
                    goBackFromReport = true
                ),
                successInfoModel = null,
                cancelInfoModel = null,
                confirmInfoModel = null
            )
        }
    }

    fun navigateBackFromImages() {
        uiState.update {
            it.copy(
                navigationModel = ReportNavigationModel(
                    goBackFromImages = true
                ),
                successInfoModel = null,
                cancelInfoModel = null,
                confirmInfoModel = null
            )
        }
    }

    // region AddFinding
    fun cancelFinding() {
        uiState.update {
            it.copy(
                navigationModel = ReportNavigationModel(
                    cancelFinding = true
                ),
                cancelInfoModel = findingCancellationBanner().mapToUi()
            )
        }
    }

    fun saveFinding() {
        val (confirmInfoModel, navigationModel) =
            if (isValidDescription && uiState.value.selectedMediaItems.isEmpty()) {
                findingConfirmationBanner().mapToUi() to null
            } else if (isValidDescription && uiState.value.selectedMediaItems.isNotEmpty()) {
                null to ReportNavigationModel(
                    closeFinding = true,
                    imagesSize = uiState.value.selectedMediaItems.size
                )
            } else {
                null to null
            }

        uiState.update {
            it.copy(
                confirmInfoModel = confirmInfoModel,
                validateFields = true,
                navigationModel = navigationModel
            )
        }
    }

    fun confirmFinding() {
        uiState.update {
            it.copy(
                successInfoModel = findingSavedBanner().mapToUi(),
                navigationModel = ReportNavigationModel(
                    closeFinding = true,
                    imagesSize = uiState.value.selectedMediaItems.size,
                    novelty = Novelty(
                        idPreoperational = findingId.orEmpty(),
                        novelty = description,
                        images = emptyList()
                    )
                )
            )
        }
    }

    fun saveFindingImages() {
        uiState.update {
            it.copy(
                confirmInfoModel = findingConfirmationBanner().mapToUi()
            )
        }
    }

    fun confirmFindingImages(images: List<File>) {
        uiState.update {
            it.copy(
                confirmInfoModel = null,
                successInfoModel = findingSavedBanner().mapToUi(),
                isLoading = false,
                navigationModel = ReportNavigationModel(
                    closeFinding = true,
                    novelty = Novelty(
                        idPreoperational = findingId.orEmpty(),
                        novelty = description,
                        images = images.mapIndexed { index, image ->
                            ImageModel(
                                fileName = "Img_$findingId" + "_$index.jpg",
                                file = image
                            )
                        }
                    )
                )
            )
        }
    }
    // endregion

    // region AddReport
    fun cancelReport() {
        uiState.update {
            it.copy(
                navigationModel = ReportNavigationModel(
                    cancelReport = true
                ),
                cancelInfoModel = reportCancellationBanner().mapToUi()
            )
        }
    }

    fun saveReport(roleName: String) {
        val (confirmInfoModel, navigationModel) = if (
            isValidTopic && isValidDescription && uiState.value.selectedMediaItems.isEmpty()
        ) {
            reportConfirmationBanner().mapToUi() to null
        } else if (isValidTopic && isValidDescription && uiState.value.selectedMediaItems.isNotEmpty()) {
            null to ReportNavigationModel(
                closeReport = true,
                imagesSize = uiState.value.selectedMediaItems.size
            )
        } else {
            null to null
        }

        uiState.update {
            it.copy(
                roleName = roleName,
                confirmInfoModel = confirmInfoModel,
                validateFields = true,
                navigationModel = navigationModel
            )
        }
    }

    fun confirmReport() {
        job?.cancel()
        job = viewModelScope.launch {
            sendReport.invoke(
                roleName = uiState.value.roleName,
                topic = topic,
                description = description,
                images = listOf()
            ).onSuccess {
                withContext(Dispatchers.Main) {
                    uiState.update {
                        it.copy(
                            confirmInfoModel = null,
                            successInfoModel = reportSentBanner().mapToUi(),
                            isLoading = false,
                            navigationModel = ReportNavigationModel(
                                closeReport = true
                            )
                        )
                    }
                }
            }.onFailure { throwable ->
                withContext(Dispatchers.Main) {
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            confirmInfoModel = null,
                            infoEvent = throwable.mapToUi()
                        )
                    }
                }
            }
        }
    }

    fun saveReportImages() {
        uiState.update {
            it.copy(
                confirmInfoModel = reportConfirmationBanner().mapToUi()
            )
        }
    }

    fun confirmReportImages(images: List<File>) {
        uiState.update { it.copy(isLoading = true) }
        job?.cancel()
        job = viewModelScope.launch {
            sendReport.invoke(
                roleName = uiState.value.roleName,
                topic = topic,
                description = description,
                images = images.mapIndexed { index, image ->
                    ImageModel(
                        fileName = "Img_$topic" + "_$index.jpg",
                        file = image
                    )
                }
            ).onSuccess {
                withContext(Dispatchers.Main) {
                    uiState.update {
                        it.copy(
                            confirmInfoModel = null,
                            successInfoModel = reportSentBanner().mapToUi(),
                            isLoading = false,
                            navigationModel = ReportNavigationModel(
                                closeReport = true
                            )
                        )
                    }
                }
            }.onFailure { throwable ->
                withContext(Dispatchers.Main) {
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            confirmInfoModel = null,
                            infoEvent = throwable.mapToUi()
                        )
                    }
                }
            }
        }
    }
    // endregion

    private fun getImageLimit(isFromPreOperational: Boolean) = if (isFromPreOperational.not()) {
        uiState.value.operationConfig?.numImgNovelty ?: 0
    } else {
        when (uiState.value.operationConfig?.operationRole) {
            OperationRole.AUXILIARY_AND_OR_TAPH ->
                uiState.value.operationConfig?.numImgPreoperationalAux ?: 0

            OperationRole.DRIVER -> uiState.value.operationConfig?.numImgPreoperationalDriver ?: 0
            OperationRole.LEAD_APH -> 0
            OperationRole.MEDIC_APH ->
                uiState.value.operationConfig?.numImgPreoperationalDoctor ?: 0

            null -> 0
        }
    }

    // region ImageConfirmation
    fun updateMediaActions(
        mediaItems: List<MediaItemUiModel>,
        isFromPreOperational: Boolean
    ) {
        val updateSelectedImages = buildList {
            addAll(uiState.value.selectedMediaItems)

            val imageLimit = getImageLimit(isFromPreOperational)
            mediaItems.forEachIndexed { index, image ->
                if (imageLimit <= (uiState.value.selectedMediaItems.size + index)) {
                    uiState.update {
                        it.copy(
                            infoEvent = imagesLimitErrorBanner(imageLimit).mapToUi()
                        )
                    }
                    return@forEachIndexed
                } else if (!image.isSizeValid) {
                    uiState.update {
                        it.copy(
                            infoEvent = fileSizeErrorBanner().mapToUi()
                        )
                    }
                    return@forEachIndexed
                } else {
                    add(image)
                }
            }
        }

        uiState.update {
            it.copy(
                selectedMediaItems = updateSelectedImages
            )
        }
    }

    fun removeCurrentImage() {
        val updateSelectedImages = buildList {
            uiState.value.selectedMediaItems.mapIndexed { index, uri ->
                if (index != currentImage) {
                    add(uri)
                }
            }
        }

        uiState.update {
            it.copy(
                selectedMediaItems = updateSelectedImages
            )
        }
    }
    // endregion

    fun showCamera(isFromPreOperational: Boolean) {
        uiState.update {
            it.copy(
                isFromPreOperational = isFromPreOperational,
                navigationModel = ReportNavigationModel(
                    showCamera = true
                )
            )
        }
    }

    fun onPhotoTaken(mediaItemUiModel: MediaItemUiModel) {
        val imageLimit = getImageLimit(
            uiState.value.isFromPreOperational
        )
        val isOverImageLimit = uiState.value.selectedMediaItems.size >= imageLimit

        val updatedSelectedImages = buildList {
            addAll(uiState.value.selectedMediaItems)

            if (!isOverImageLimit && mediaItemUiModel.isSizeValid) add(mediaItemUiModel)
        }

        val invalidMediaSelected = !mediaItemUiModel.isSizeValid

        uiState.update {
            it.copy(
                selectedMediaItems = updatedSelectedImages,
                navigationModel = ReportNavigationModel(
                    photoTaken = true
                ),
                infoEvent = if (isOverImageLimit) {
                    imagesLimitErrorBanner(imageLimit).mapToUi()
                } else if (invalidMediaSelected) {
                    fileSizeErrorBanner().mapToUi()
                } else {
                    null
                }
            )
        }
    }

    fun consumeShownConfirm() {
        uiState.update {
            it.copy(
                confirmInfoModel = null
            )
        }
    }

    fun consumeShownError() {
        uiState.update {
            it.copy(
                infoEvent = null
            )
        }
    }

    fun consumeNavigationEvent() {
        uiState.update {
            it.copy(
                validateFields = false,
                cancelInfoModel = null,
                confirmInfoModel = null,
                navigationModel = null
            )
        }
    }
}
