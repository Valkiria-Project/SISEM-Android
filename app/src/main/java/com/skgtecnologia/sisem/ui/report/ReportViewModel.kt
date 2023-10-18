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
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val observeOperationConfig: ObserveOperationConfig,
    private val sendReport: SendReport
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(ReportUiState())
        private set

    var findingId by mutableIntStateOf(0)
    var topic by mutableStateOf("")
    var description by mutableStateOf("")
    var isValidTopic by mutableStateOf(false)
    var isValidDescription by mutableStateOf(false)
    var currentImage by mutableIntStateOf(0)

    private val imageLimit
        get() = when (uiState.operationModel?.operationRole) {
            OperationRole.AUXILIARY_AND_OR_TAPH ->
                uiState.operationModel?.numImgPreoperationalAux ?: 0

            OperationRole.DRIVER -> uiState.operationModel?.numImgPreoperationalDriver ?: 0
            OperationRole.LEAD_APH -> 0
            OperationRole.MEDIC_APH -> uiState.operationModel?.numImgPreoperationalDoctor ?: 0
            null -> 0
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
                        errorModel = throwable.mapToUi()
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
        val navigationModel = if (isValidDescription) {
            ReportNavigationModel(
                closeFinding = true,
                imagesSize = uiState.selectedImageUris.size
            )
        } else {
            null
        }

        uiState = uiState.copy(
            validateFields = true,
            navigationModel = navigationModel
        )
    }

    fun confirmFindingImages() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                confirmFinding = true
            ),
            confirmInfoModel = findingConfirmationBanner().mapToUi()
        )
    }

    fun saveFindingWithImages(images: List<String>) {
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
        val navigationModel = if (isValidTopic && isValidDescription) {
            ReportNavigationModel(
                closeReport = true,
                imagesSize = uiState.selectedImageUris.size
            )
        } else {
            null
        }
        uiState = uiState.copy(
            validateFields = true,
            navigationModel = navigationModel
        )
    }

    fun confirmReportImages() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                confirmSendReport = true
            ),
            confirmInfoModel = reportConfirmationBanner().mapToUi()
        )
    }

    fun sendReport(images: List<String>) {
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
                        errorModel = throwable.mapToUi()
                    )
                }
            }
        }
    }
    // endregion

    // region ImageConfirmation
    fun updateSelectedImages(selectedImages: List<Uri>) {
        val updateSelectedImages = buildList {

            addAll(uiState.selectedImageUris)

            selectedImages.forEachIndexed { index, image ->
                if (imageLimit <= (uiState.selectedImageUris.size + index)) {
                    uiState = uiState.copy(
                        errorModel = imagesLimitErrorBanner(imageLimit).mapToUi()
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

    fun showCamera() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                showCamera = true
            )
        )
    }

    fun onPhotoTaken(savedUri: Uri) {
        val updatedSelectedImages = buildList {
            addAll(uiState.selectedImageUris)

            if (imageLimit < uiState.selectedImageUris.size) {
                uiState = uiState.copy(
                    errorModel = imagesLimitErrorBanner(imageLimit).mapToUi()
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
            errorModel = null
        )
    }

    fun consumeShownConfirm() {
        uiState = uiState.copy(
            confirmInfoModel = null
        )
    }
}
