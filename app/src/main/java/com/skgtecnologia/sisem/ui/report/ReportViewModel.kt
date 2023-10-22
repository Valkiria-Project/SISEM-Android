package com.skgtecnologia.sisem.ui.report

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.di.operation.OperationRole
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
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("TooManyFunctions")
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val observeOperationConfig: ObserveOperationConfig,
    private val sendReport: SendReport
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(ReportUiState())
        private set

    var findingId by mutableStateOf("")
    var topic by mutableStateOf("")
    var description by mutableStateOf("")
    var isValidTopic by mutableStateOf(false)
    var isValidDescription by mutableStateOf(false)
    var currentImage by mutableIntStateOf(0)

    private fun getImageLimit(isFromPreOperational: Boolean) = if (isFromPreOperational.not()) {
        uiState.operationModel?.numImgNovelty ?: 0
    } else {
        when (uiState.operationModel?.operationRole) {
            OperationRole.AUXILIARY_AND_OR_TAPH ->
                uiState.operationModel?.numImgPreoperationalAux ?: 0

            OperationRole.DRIVER -> uiState.operationModel?.numImgPreoperationalDriver ?: 0
            OperationRole.LEAD_APH -> 0
            OperationRole.MEDIC_APH -> uiState.operationModel?.numImgPreoperationalDoctor ?: 0
            null -> 0
        }
    }

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            observeOperationConfig.invoke()
                .onSuccess { operationModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            operationModel = operationModel,
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
            if (isValidDescription && uiState.selectedImageUris.isNotEmpty()) {
                null to ReportNavigationModel(
                    closeFinding = true,
                    imagesSize = uiState.selectedImageUris.size
                )
            } else {
                findingConfirmationBanner().mapToUi() to null
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
                imagesSize = uiState.selectedImageUris.size,
                novelty = Novelty(
                    idPreoperational = findingId,
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
                    idPreoperational = findingId,
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

    fun saveReport() {
        val (confirmInfoModel, navigationModel) = if (
            isValidTopic && isValidDescription && uiState.selectedImageUris.isEmpty()
        ) {
            reportConfirmationBanner().mapToUi() to null
        } else if (isValidTopic && isValidDescription && uiState.selectedImageUris.isNotEmpty()) {
            null to ReportNavigationModel(
                closeReport = true,
                imagesSize = uiState.selectedImageUris.size
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

    fun confirmReport() {
        uiState = uiState.copy(
            successInfoModel = reportSentBanner().mapToUi(),
            navigationModel = ReportNavigationModel(
                closeReport = true
            )
        )
    }

    fun saveReportImages() {
        uiState = uiState.copy(
            confirmInfoModel = reportConfirmationBanner().mapToUi()
        )
    }

    fun confirmReportImages(images: List<File>) {
        uiState = uiState.copy(isLoading = true)
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            sendReport.invoke(
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

    // region ImageConfirmation
    fun updateSelectedImages(selectedImages: List<Uri>, isFromPreOperational: Boolean) {
        val updateSelectedImages = buildList {

            addAll(uiState.selectedImageUris)

            val imageLimit = getImageLimit(isFromPreOperational)
            selectedImages.forEachIndexed { index, image ->
                if (imageLimit <= (uiState.selectedImageUris.size + index)) {
                    uiState = uiState.copy(
                        infoEvent = imagesLimitErrorBanner(imageLimit).mapToUi()
                    )
                    return@forEachIndexed
                } else {
                    add(image)
                }
            }
        }

        uiState = uiState.copy(
            selectedImageUris = updateSelectedImages
        )
    }

    fun removeCurrentImage() {
        val updateSelectedImages = buildList {
            uiState.selectedImageUris.mapIndexed { index, uri ->
                if (index != currentImage) {
                    add(uri)
                }
            }
        }

        uiState = uiState.copy(
            selectedImageUris = updateSelectedImages
        )
    }
    // endregion

    fun showCamera(isFromPreOperational: Boolean) {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                isFromPreOperational = isFromPreOperational,
                showCamera = true
            )
        )
    }

    fun onPhotoTaken(savedUri: Uri) {
        val updatedSelectedImages = buildList {
            addAll(uiState.selectedImageUris)

            val imageLimit = getImageLimit(uiState.navigationModel?.isFromPreOperational == true)
            if (imageLimit < uiState.selectedImageUris.size) {
                uiState = uiState.copy(
                    infoEvent = imagesLimitErrorBanner(imageLimit).mapToUi()
                )
            } else {
                add(savedUri)
            }
        }

        uiState = uiState.copy(
            selectedImageUris = updatedSelectedImages,
            navigationModel = ReportNavigationModel(
                photoTaken = true
            )
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

    fun consumeShownError() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }

    fun consumeShownConfirm() {
        uiState = uiState.copy(
            confirmInfoModel = null
        )
    }
}
