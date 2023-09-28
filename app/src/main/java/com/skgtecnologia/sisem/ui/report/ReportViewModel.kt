package com.skgtecnologia.sisem.ui.report

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.model.banner.findingCancellationBanner
import com.skgtecnologia.sisem.domain.model.banner.reportCancellationBanner
import com.skgtecnologia.sisem.domain.model.banner.reportConfirmationBanner
import com.skgtecnologia.sisem.domain.model.banner.findingSavedBanner
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.reportSentBanner
import com.skgtecnologia.sisem.domain.operation.usecases.RetrieveOperationConfig
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.skgtecnologia.sisem.domain.report.usecases.SendReport
import com.skgtecnologia.sisem.ui.navigation.model.ReportNavigationModel
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val retrieveOperationConfig: RetrieveOperationConfig,
    private val sendReport: SendReport
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(ReportUiState())
        private set

    var topic by mutableStateOf("")
    var description by mutableStateOf("")
    var isValidTopic by mutableStateOf(false)
    var isValidDescription by mutableStateOf(false)

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            retrieveOperationConfig.invoke()
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

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                goBack = true
            ),
            cancelInfoModel = null
        )
    }

    fun cancelFinding() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                cancelFinding = true
            ),
            cancelInfoModel = findingCancellationBanner().mapToUi()
        )
    }

    fun cancelReport() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                cancelReport = true
            ),
            cancelInfoModel = reportCancellationBanner()
                .mapToUi()
        )
    }

    fun updateSelectedImages(selectedImages: List<Uri>, role: String? = null) {
        val imageLimit = when (role) {
            "Conductor" -> uiState.operationModel?.numImgPreoperationalDriver ?: 0
            "Médico" -> uiState.operationModel?.numImgPreoperationalDoctor ?: 0
            "Auxiliar" -> uiState.operationModel?.numImgPreoperationalAux ?: 0
            else -> 0
        }

        val updateSelectedImages = buildList {
            addAll(uiState.selectedImageUris)

            selectedImages.forEachIndexed { index, image ->
                if (imageLimit < uiState.selectedImageUris.size + index + 1) {
                    uiState = uiState.copy(
                        errorModel = BannerUiModel(
                            icon = "ic_alert",
                            title = "Cantidad de fotos",
                            description = """Se ha excedido el número de imágenes permitido por
                                | el sistema $imageLimit""".trimMargin()
                        )
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

    fun showCamera() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                showCamera = true
            )
        )
    }

    @Suppress("MagicNumber")
    fun onPhotoTaken(savedUri: Uri, role: String? = null) {
        val imageLimit = when (role) {
            "Conductor" -> uiState.operationModel?.numImgPreoperationalDriver ?: 0
            "Médico" -> uiState.operationModel?.numImgPreoperationalDoctor ?: 0
            "Auxiliar" -> uiState.operationModel?.numImgPreoperationalAux ?: 0
            else -> 3
        }

        val updatedSelectedImages = buildList {
            uiState.selectedImageUris.forEach {
                add(it)
            }

            if (imageLimit < uiState.selectedImageUris.size + 1) {
                uiState = uiState.copy(
                    errorModel = BannerUiModel(
                        icon = "ic_alert",
                        title = "Cantidad de fotos",
                        description = """Se ha excedido el número de imágenes permitido por
                                | el sistema $imageLimit""".trimMargin()
                    )
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

    fun confirmSendFinding() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                confirmFinding = true
            ),
            confirmInfoModel = findingSavedBanner().mapToUi()
        )
    }

    fun confirmSendReport() {
        uiState = uiState.copy(
            navigationModel = ReportNavigationModel(
                confirmSendReport = true
            ),
            confirmInfoModel = reportConfirmationBanner().mapToUi()
        )
    }

    @Suppress("UnusedPrivateMember")
    fun saveFindingWithImages(images: List<String>? = null) {
        // FIXME: Save to the database with a key and retrieve this afterwards
        uiState = uiState.copy(
            confirmInfoModel = null,
            successInfoModel = findingSavedBanner().mapToUi(),
            isLoading = false,
            navigationModel = ReportNavigationModel(
                closeFinding = true
            )
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

    fun handleNavigation() {
        uiState = uiState.copy(
            validateFields = false,
            cancelInfoModel = null,
            confirmInfoModel = null,
            navigationModel = null
        )
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }

    fun handleShownConfirm() {
        uiState = uiState.copy(
            confirmInfoModel = null
        )
    }
}
