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

    var uiState by mutableStateOf(ReportUiState())
        private set

    private val findingId = savedStateHandle.toRoute<ReportRoute.AddFindingRoute>().findingId

    var topic by mutableStateOf("")
    var description by mutableStateOf("")
    var isValidTopic by mutableStateOf(false)
    var isValidDescription by mutableStateOf(false)
    var currentImage by mutableIntStateOf(0)

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            observeOperationConfig.invoke()
                .onSuccess { operationModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            operationConfig = operationModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(
                        isLoading = false,
                        infoEvent = throwable.mapToUi()
                    )
                }
        }
    }

    fun navigateBackFromReport() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                goBackFromReport = true
            ),
            successInfoModel = null,
            cancelInfoModel = null,
            confirmInfoModel = null
        )
    }

    fun navigateBackFromImages() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                goBackFromImages = true
            ),
            successInfoModel = null,
            cancelInfoModel = null,
            confirmInfoModel = null
        )
    }

    // region AddFinding
    fun cancelFinding() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                cancelFinding = true
            ),
            cancelInfoModel = findingCancellationBanner().mapToUi()
        )
    }

    fun saveFinding() {
        val (confirmInfoModel, navigationModel) =
            if (isValidDescription && uiState.selectedMediaItems.isEmpty()) {
                findingConfirmationBanner().mapToUi() to null
            } else if (isValidDescription && uiState.selectedMediaItems.isNotEmpty()) {
                null to ReportNavigationModel(
                    closeFinding = true,
                    imagesSize = uiState.selectedMediaItems.size
                )
            } else {
                null to null
            }

        uiState = uiState.copy(
            confirmInfoModel = confirmInfoModel,
            validateFields = true,
            navigationModel = navigationModel
        )
    }

    fun confirmFinding() {
        uiState = uiState.copy(
            successInfoModel = findingSavedBanner().mapToUi(),
            navigationModel = ReportNavigationModel(
                closeFinding = true,
                imagesSize = uiState.selectedMediaItems.size,
                novelty = Novelty(
                    idPreoperational = findingId.orEmpty(),
                    novelty = description,
                    images = emptyList()
                )
            )
        )
    }

    fun saveFindingImages() {
        uiState = uiState.copy(
            confirmInfoModel = findingConfirmationBanner().mapToUi()
        )
    }

    fun confirmFindingImages(images: List<File>) {
        uiState = uiState.copy(
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
    // endregion

    // region AddReport
    fun cancelReport() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                cancelReport = true
            ),
            cancelInfoModel = reportCancellationBanner().mapToUi()
        )
    }

    fun saveReport(roleName: String) {
        val (confirmInfoModel, navigationModel) = if (
            isValidTopic && isValidDescription && uiState.selectedMediaItems.isEmpty()
        ) {
            reportConfirmationBanner().mapToUi() to null
        } else if (isValidTopic && isValidDescription && uiState.selectedMediaItems.isNotEmpty()) {
            null to ReportNavigationModel(
                closeReport = true,
                imagesSize = uiState.selectedMediaItems.size
            )
        } else {
            null to null
        }

        uiState = uiState.copy(
            roleName = roleName,
            confirmInfoModel = confirmInfoModel,
            validateFields = true,
            navigationModel = navigationModel
        )
    }

    fun confirmReport() {
        job?.cancel()
        job = viewModelScope.launch {
            sendReport.invoke(
                roleName = uiState.roleName,
                topic = topic,
                description = description,
                images = listOf()
            ).onSuccess {
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        confirmInfoModel = null,
                        successInfoModel = reportSentBanner().mapToUi(),
                        isLoading = false,
                        navigationModel = ReportNavigationModel(
                            closeReport = true
                        )
                    )
                }
            }.onFailure { throwable ->
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        isLoading = false,
                        confirmInfoModel = null,
                        infoEvent = throwable.mapToUi()
                    )
                }
            }
        }
    }

    fun saveReportImages() {
        uiState = uiState.copy(
            confirmInfoModel = reportConfirmationBanner().mapToUi()
        )
    }

    fun confirmReportImages(images: List<File>) {
        uiState = uiState.copy(isLoading = true)
        job?.cancel()
        job = viewModelScope.launch {
            sendReport.invoke(
                roleName = uiState.roleName,
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
                    uiState = uiState.copy(
                        confirmInfoModel = null,
                        successInfoModel = reportSentBanner().mapToUi(),
                        isLoading = false,
                        navigationModel = ReportNavigationModel(
                            closeReport = true
                        )
                    )
                }
            }.onFailure { throwable ->
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        isLoading = false,
                        confirmInfoModel = null,
                        infoEvent = throwable.mapToUi()
                    )
                }
            }
        }
    }
    // endregion

    private fun getImageLimit(isFromPreOperational: Boolean) = if (isFromPreOperational.not()) {
        uiState.operationConfig?.numImgNovelty ?: 0
    } else {
        when (uiState.operationConfig?.operationRole) {
            OperationRole.AUXILIARY_AND_OR_TAPH ->
                uiState.operationConfig?.numImgPreoperationalAux ?: 0

            OperationRole.DRIVER -> uiState.operationConfig?.numImgPreoperationalDriver ?: 0
            OperationRole.LEAD_APH -> 0
            OperationRole.MEDIC_APH -> uiState.operationConfig?.numImgPreoperationalDoctor ?: 0
            null -> 0
        }
    }

    // region ImageConfirmation
    fun updateMediaActions(
        mediaItems: List<MediaItemUiModel>,
        isFromPreOperational: Boolean
    ) {
        val updateSelectedImages = buildList {
            addAll(uiState.selectedMediaItems)

            val imageLimit = getImageLimit(isFromPreOperational)
            mediaItems.forEachIndexed { index, image ->
                if (imageLimit <= (uiState.selectedMediaItems.size + index)) {
                    uiState = uiState.copy(
                        infoEvent = imagesLimitErrorBanner(imageLimit).mapToUi()
                    )
                    return@forEachIndexed
                } else if (!image.isSizeValid) {
                    uiState = uiState.copy(
                        infoEvent = fileSizeErrorBanner().mapToUi()
                    )
                    return@forEachIndexed
                } else {
                    add(image)
                }
            }
        }

        uiState = uiState.copy(
            selectedMediaItems = updateSelectedImages
        )
    }

    fun removeCurrentImage() {
        val updateSelectedImages = buildList {
            uiState.selectedMediaItems.mapIndexed { index, uri ->
                if (index != currentImage) {
                    add(uri)
                }
            }
        }

        uiState = uiState.copy(
            selectedMediaItems = updateSelectedImages
        )
    }
    // endregion

    fun showCamera(isFromPreOperational: Boolean) {
        uiState = uiState.copy(
            isFromPreOperational = isFromPreOperational,
            navigationModel = ReportNavigationModel(
                showCamera = true
            )
        )
    }

    fun onPhotoTaken(mediaItemUiModel: MediaItemUiModel) {
        val imageLimit = getImageLimit(
            uiState.isFromPreOperational
        )
        val isOverImageLimit = uiState.selectedMediaItems.size >= imageLimit

        val updatedSelectedImages = buildList {
            addAll(uiState.selectedMediaItems)

            if (!isOverImageLimit && mediaItemUiModel.isSizeValid) add(mediaItemUiModel)
        }

        val invalidMediaSelected = !mediaItemUiModel.isSizeValid

        uiState = uiState.copy(
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

    fun consumeShownConfirm() {
        uiState = uiState.copy(
            confirmInfoModel = null
        )
    }

    fun consumeShownError() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            validateFields = false,
            cancelInfoModel = null,
            confirmInfoModel = null,
            navigationModel = null
        )
    }
}
