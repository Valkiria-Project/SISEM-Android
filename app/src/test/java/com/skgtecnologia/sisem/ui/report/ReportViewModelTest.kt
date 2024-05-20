package com.skgtecnologia.sisem.ui.report

import com.skgtecnologia.sisem.commons.DRIVER_ROLE
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.domain.report.usecases.SendReport
import com.valkiria.uicomponents.components.media.MediaItemUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val FINDING_BANNER_CONFIRM_TITLE = "Guardar hallazgo"
private const val FINDING_BANNER_SAVED_TITLE = "Hallazgo guardado"
private const val NOVELTY_BANNER_CANCEL_TITLE = "Cancelar novedad"
private const val NOVELTY_BANNER_CONFIRM_TITLE = "Guardar novedad"
private const val NOVELTY_BANNER_SAVED_TITLE = "Novedad guardada"
private const val BANNER_LIMIT_TITLE = "Cantidad de fotos"

class ReportViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var observeOperationConfig: ObserveOperationConfig

    @MockK
    private lateinit var sendReport: SendReport

    private lateinit var viewModel: ReportViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when observeOperationConfig is success`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)

        Assert.assertEquals(operationModel, viewModel.uiState.operationConfig)
    }

    @Test
    fun `when observeOperationConfig is failure`() = runTest {
        val exception = mockk<Exception>()
        coEvery { observeOperationConfig.invoke() } returns Result.failure(exception)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when navigateBackFromReport is called goBackFromReport should be true`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.navigateBackFromReport()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.goBackFromReport)
    }

    @Test
    fun `when navigateBackFromImages is called goBackFromImages should be true`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.navigateBackFromImages()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.goBackFromImages)
    }

    @Test
    fun `when cancelFinding is called cancelFinding should be true`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.cancelFinding()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.cancelFinding)
    }

    @Test
    fun `when saveFinding is called without validations should be null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.saveFinding()

        Assert.assertEquals(null, viewModel.uiState.confirmInfoModel)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when saveFinding is called with empty uris confirmation model is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.isValidDescription = true
        viewModel.saveFinding()

        Assert.assertEquals(FINDING_BANNER_CONFIRM_TITLE, viewModel.uiState.confirmInfoModel?.title)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when saveFinding is called with uris navigation model is not null`() = runTest {
        val operationModel = mockk<OperationModel> {
            every { numImgNovelty } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.isValidDescription = true
        viewModel.updateMediaActions(
            listOf(
                MediaItemUiModel(
                    uri = "APH_FILE_254_1000076492.jpg",
                    name = "1000076492.jpg",
                    isSizeValid = true
                ),
                MediaItemUiModel(
                    "APH_FILE_254_1000076490.jpg",
                    name = "1000076490.jpg",
                    isSizeValid = true
                )
            ),
            false
        )
        viewModel.saveFinding()

        Assert.assertEquals(null, viewModel.uiState.confirmInfoModel)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.closeFinding)
        Assert.assertEquals(1, viewModel.uiState.navigationModel?.imagesSize)
    }

    @Test
    fun `when confirmFinding is called successInfoModel is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.confirmFinding()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.closeFinding)
        Assert.assertEquals(FINDING_BANNER_SAVED_TITLE, viewModel.uiState.successInfoModel?.title)
    }

    @Test
    fun `when saveFindingImages is called confirmInfoModel is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.saveFindingImages()

        Assert.assertEquals(FINDING_BANNER_CONFIRM_TITLE, viewModel.uiState.confirmInfoModel?.title)
    }

    @Test
    fun `when confirmFindingImages is called successInfoModel is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.confirmFindingImages(listOf(mockk()))

        Assert.assertEquals(FINDING_BANNER_SAVED_TITLE, viewModel.uiState.successInfoModel?.title)
    }

    @Test
    fun `when cancelReport is called cancelInfoModel is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.cancelReport()

        Assert.assertEquals(NOVELTY_BANNER_CANCEL_TITLE, viewModel.uiState.cancelInfoModel?.title)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.cancelReport)
    }

    @Test
    fun `when saveReport is called without validations should be null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.saveReport(DRIVER_ROLE)

        Assert.assertEquals(null, viewModel.uiState.confirmInfoModel)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when saveReport is called with empty uris confirmation model is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.isValidDescription = true
        viewModel.isValidTopic = true
        viewModel.saveReport(DRIVER_ROLE)

        Assert.assertEquals(NOVELTY_BANNER_CONFIRM_TITLE, viewModel.uiState.confirmInfoModel?.title)
    }

    @Test
    fun `when saveReport is called with uris navigationModel is not null`() = runTest {
        val operationModel = mockk<OperationModel> {
            every { numImgNovelty } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.isValidDescription = true
        viewModel.isValidTopic = true
        viewModel.updateMediaActions(
            listOf(
                MediaItemUiModel(
                    uri = "APH_FILE_254_1000076492.jpg",
                    name = "1000076492.jpg",
                    isSizeValid = true
                ),
                MediaItemUiModel(
                    "APH_FILE_254_1000076490.jpg",
                    name = "1000076490.jpg",
                    isSizeValid = true
                )
            ),
            false
        )
        viewModel.saveReport(DRIVER_ROLE)

        Assert.assertEquals(null, viewModel.uiState.confirmInfoModel)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.closeReport)
        Assert.assertEquals(1, viewModel.uiState.navigationModel?.imagesSize)
    }

    @Test
    fun `when confirmReport is called successInfoModel is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.confirmReport()

        Assert.assertEquals(NOVELTY_BANNER_SAVED_TITLE, viewModel.uiState.successInfoModel?.title)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.closeReport)
    }

    @Test
    fun `when saveReportImages is called confirmInfoModel is not null`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.saveReportImages()

        Assert.assertEquals(NOVELTY_BANNER_CONFIRM_TITLE, viewModel.uiState.confirmInfoModel?.title)
    }

    @Test
    fun `when confirmReportImages and sendReport is success`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { sendReport.invoke(any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.confirmReportImages(listOf(mockk()))

        Assert.assertEquals(NOVELTY_BANNER_SAVED_TITLE, viewModel.uiState.successInfoModel?.title)
    }

    @Test
    fun `when confirmReportImages and sendReport fails`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery {
            sendReport.invoke(
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.failure(Throwable())

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.confirmReportImages(listOf())

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when updateSelectedImages is called with isFromPreOperational false`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { numImgNovelty } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, false)

        Assert.assertEquals(BANNER_LIMIT_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when updateSelectedImages with isFromPreOperational true and auxiliary`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns OperationRole.AUXILIARY_AND_OR_TAPH
            every { numImgPreoperationalAux } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)

        Assert.assertEquals(BANNER_LIMIT_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when updateSelectedImages with isFromPreOperational true and driver`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns OperationRole.DRIVER
            every { numImgPreoperationalDriver } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)

        Assert.assertEquals(BANNER_LIMIT_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when updateSelectedImages with isFromPreOperational true and lead aph`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns OperationRole.LEAD_APH
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)

        Assert.assertEquals(BANNER_LIMIT_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when updateSelectedImages with isFromPreOperational true and medic aph`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns OperationRole.MEDIC_APH
            every { numImgPreoperationalDoctor } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)

        Assert.assertEquals(BANNER_LIMIT_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when updateSelectedImages with isFromPreOperational true and null role`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns null
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)

        Assert.assertEquals(BANNER_LIMIT_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when removeCurrentImage is called`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns OperationRole.MEDIC_APH
            every { numImgPreoperationalDoctor } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)
        viewModel.currentImage = 1
        viewModel.removeCurrentImage()

        Assert.assertNotNull(viewModel.uiState.selectedMediaItems)
    }

    @Test
    fun `when showCamera is called with true`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.showCamera(true)

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.showCamera)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isFromPreOperational)
    }

    @Test
    fun `when showCamera is called with false`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.showCamera(false)

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.showCamera)
        Assert.assertEquals(false, viewModel.uiState.navigationModel?.isFromPreOperational)
    }

    @Test
    fun `when onPhotoTaken is called with one uri`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns OperationRole.MEDIC_APH
            every { numImgPreoperationalDoctor } returns 1
            every { numImgNovelty } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)
        viewModel.onPhotoTaken(
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.photoTaken)
    }

    @Test
    fun `when onPhotoTaken is called with two uris`() = runTest {
        val selectedImages = listOf(
            MediaItemUiModel(
                uri = "APH_FILE_254_1000076492.jpg",
                name = "1000076492.jpg",
                isSizeValid = true
            ),
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )
        val operationModel = mockk<OperationModel> {
            every { operationRole } returns OperationRole.MEDIC_APH
            every { numImgPreoperationalDoctor } returns 2
            every { numImgNovelty } returns 1
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.updateMediaActions(selectedImages, true)
        viewModel.onPhotoTaken(
            MediaItemUiModel(
                "APH_FILE_254_1000076490.jpg",
                name = "1000076490.jpg",
                isSizeValid = true
            )
        )

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.photoTaken)
        Assert.assertEquals(BANNER_LIMIT_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when consumeShownConfirm is called`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.consumeShownConfirm()

        Assert.assertEquals(null, viewModel.uiState.confirmInfoModel)
    }

    @Test
    fun `when consumeShownError is called`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.consumeShownError()

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)

        viewModel = ReportViewModel(observeOperationConfig, sendReport)
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(false, viewModel.uiState.validateFields)
        Assert.assertEquals(null, viewModel.uiState.cancelInfoModel)
        Assert.assertEquals(null, viewModel.uiState.confirmInfoModel)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }
}
