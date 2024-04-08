package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.lifecycle.SavedStateHandle
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.chipOptionsUiModelMock
import com.skgtecnologia.sisem.commons.chipSelectionUiModelMock
import com.skgtecnologia.sisem.commons.dropDownUiModelMock
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.imageButtonSectionUiModelMock
import com.skgtecnologia.sisem.commons.infoCardUiModelMock
import com.skgtecnologia.sisem.commons.labelUiModelMock
import com.skgtecnologia.sisem.commons.mediaActionsUiModelMock
import com.skgtecnologia.sisem.commons.medsSelectorUiModelMock
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.commons.segmentedSwitchUiModelMock
import com.skgtecnologia.sisem.commons.signatureUiModelMock
import com.skgtecnologia.sisem.commons.sliderUiModelMock
import com.skgtecnologia.sisem.commons.textFieldUiModelMock
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.medicalhistory.model.ALIVE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DURING_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.FC_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.FUR_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_MRO_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_RTS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_TOTAL_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.INITIAL_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_FUR_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_WEEKS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.TAS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryScreen
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SendMedicalHistory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.ui.humanbody.area.FrontArea
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyType
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val IDENTIFIER = "identifier"
private const val MEDICAL_APH_BANNER_TITLE = "Registro APH"

@Suppress("LargeClass")
class MedicalHistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var getMedicalHistoryScreen: GetMedicalHistoryScreen

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var sendMedicalHistory: SendMedicalHistory

    @MockK
    private lateinit var observeOperationConfig: ObserveOperationConfig

    @MockK
    private lateinit var stringProvider: StringProvider

    private val savedStateHandle = SavedStateHandle(
        mapOf(NavigationArgument.ID_APH to ID_APH)
    )

    private lateinit var viewModel: MedicalHistoryViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
        coEvery { observeOperationConfig.invoke() } returns mockk()
    }

    @Test
    fun `when getMedicalHistoryScreen is success`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                chipOptionsUiModelMock,
                chipSelectionUiModelMock,
                dropDownUiModelMock,
                segmentedSwitchUiModelMock,
                sliderUiModelMock,
                textFieldUiModelMock,
                infoCardUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )

        Assert.assertEquals(screenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getMedicalHistoryScreen is failure`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.failure(
            Throwable()
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorEvent?.title)
    }

    @Test
    fun `when handleChipOptionAction is called with not registered data`() = runTest {
        val chipOptionUiModel = ChipOptionUiModel(
            id = IDENTIFIER,
            name = "name",
            selected = true
        )
        val screenModel = ScreenModel(
            body = listOf(chipOptionsUiModelMock)
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleChipOptionAction(
            GenericUiAction.ChipOptionAction(
                IDENTIFIER,
                chipOptionUiModel
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleChipOptionAction with same registered data should be remove`() = runTest {
        val chipOptionUiModel = ChipOptionUiModel(
            id = IDENTIFIER,
            name = "name",
            selected = true
        )
        val screenModel = ScreenModel(
            body = listOf(
                textFieldUiModelMock,
                chipOptionsUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleChipOptionAction(
            GenericUiAction.ChipOptionAction(
                IDENTIFIER,
                chipOptionUiModel
            )
        )
        viewModel.handleChipOptionAction(
            GenericUiAction.ChipOptionAction(
                IDENTIFIER,
                chipOptionUiModel
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleChipOptionAction with registered data should be add`() = runTest {
        val chipOptionUiModel = ChipOptionUiModel(
            id = IDENTIFIER,
            name = "name",
            selected = true
        )
        val chipOptionUiModel2 = ChipOptionUiModel(
            id = "other",
            name = "name",
            selected = true
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleChipOptionAction(
            GenericUiAction.ChipOptionAction(
                IDENTIFIER,
                chipOptionUiModel
            )
        )
        viewModel.handleChipOptionAction(
            GenericUiAction.ChipOptionAction(
                IDENTIFIER,
                chipOptionUiModel2
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleChipSelectionAction is called without glassgow key`() = runTest {
        val chipSelectionItemUiModel = ChipSelectionItemUiModel(
            id = IDENTIFIER,
            name = "name"
        )
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                chipOptionsUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleChipSelectionAction(
            GenericUiAction.ChipSelectionAction(
                IDENTIFIER,
                chipSelectionItemUiModel,
                true,
                mapOf()
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleChipSelectionAction is called with glasgow key`() = runTest {
        val identifier = GLASGOW_MRO_KEY
        val chipSelectionItemUiModel = ChipSelectionItemUiModel(
            id = identifier,
            name = "name"
        )
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                chipOptionsUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleChipSelectionAction(
            GenericUiAction.ChipSelectionAction(
                identifier,
                chipSelectionItemUiModel,
                true,
                mapOf()
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleChipSelectionAction is called with glasgow and vitalSigns key`() = runTest {
        val identifier = GLASGOW_MRO_KEY
        val chipSelectionItemUiModel = ChipSelectionItemUiModel(
            id = identifier,
            name = "name"
        )
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                chipOptionsUiModelMock,
                infoCardUiModelMock.copy(
                    identifier = INITIAL_VITAL_SIGNS,
                    chipSection = ChipSectionUiModel(
                        title = TextUiModel(
                            text = "title",
                            textStyle = TextStyle.BODY_1
                        ),
                        listText = ListTextUiModel(
                            texts = listOf("$TAS_KEY 10", "$FC_KEY 10"),
                            textStyle = TextStyle.BODY_1
                        )
                    )
                ),
                labelUiModelMock.copy(identifier = GLASGOW_TOTAL_KEY),
                labelUiModelMock.copy(identifier = GLASGOW_RTS_KEY)
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleChipSelectionAction(
            GenericUiAction.ChipSelectionAction(
                identifier,
                chipSelectionItemUiModel,
                true,
                mapOf()
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleDropDownAction is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                dropDownUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleDropDownAction(
            GenericUiAction.DropDownAction(
                identifier = IDENTIFIER,
                id = IDENTIFIER,
                name = "name",
                quantity = 0,
                fieldValidated = false
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleHumanBodyAction is called`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleHumanBodyAction(
            GenericUiAction.HumanBodyAction(
                identifier = IDENTIFIER,
                values = HumanBodyUi(
                    type = HumanBodyType.FRONT.name,
                    area = FrontArea.ABDOMEN.name,
                    areaName = FrontArea.ABDOMEN.name,
                    wounds = listOf("wound1", "wound2")
                )
            )
        )
        viewModel.handleHumanBodyAction(
            GenericUiAction.HumanBodyAction(
                identifier = IDENTIFIER,
                values = HumanBodyUi(
                    type = HumanBodyType.FRONT.name,
                    area = FrontArea.ABDOMEN.name,
                    areaName = FrontArea.ABDOMEN.name,
                    wounds = listOf("wound1", "wound2")
                )
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleImageButtonAction is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                imageButtonSectionUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleImageButtonAction(
            GenericUiAction.ImageButtonAction(
                identifier = IDENTIFIER,
                itemIdentifier = IDENTIFIER
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when showVitalSignsForm is called`() = runTest {
        val identifier = DURING_VITAL_SIGNS
        val screenModel = ScreenModel(
            body = listOf(
                segmentedSwitchUiModelMock.copy(identifier = ALIVE_KEY)
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.showVitalSignsForm(identifier)

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isInfoCardEvent)
    }

    @Test
    fun `when handleInputAction is called`() = runTest {
        val identifier = FUR_KEY
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                textFieldUiModelMock.copy(identifier = identifier),
                textFieldUiModelMock,
                labelUiModelMock.copy(identifier = PREGNANT_FUR_KEY),
                labelUiModelMock.copy(identifier = PREGNANT_WEEKS_KEY)
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleInputAction(
            GenericUiAction.InputAction(
                identifier = identifier,
                updatedValue = "13/11/2023",
                fieldValidated = false,
                required = false
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleSegmentedSwitchAction is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                segmentedSwitchUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleSegmentedSwitchAction(
            GenericUiAction.SegmentedSwitchAction(
                identifier = IDENTIFIER,
                status = true,
                viewsVisibility = mapOf()
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleSliderAction is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                chipSelectionUiModelMock,
                sliderUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleSliderAction(
            GenericUiAction.SliderAction(
                identifier = IDENTIFIER,
                value = 1
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when updateVitalSignsInfoCard is called`() = runTest {
        val identifierVitalSigns = INITIAL_VITAL_SIGNS
        val screenModel = ScreenModel(
            body = listOf(
                segmentedSwitchUiModelMock.copy(identifier = ALIVE_KEY),
                infoCardUiModelMock.copy(
                    identifier = INITIAL_VITAL_SIGNS,
                    chipSection = ChipSectionUiModel(
                        title = TextUiModel(
                            text = "title",
                            textStyle = TextStyle.BODY_1
                        ),
                        listText = ListTextUiModel(
                            texts = listOf("$TAS_KEY 10", "$FC_KEY 10"),
                            textStyle = TextStyle.BODY_1
                        )
                    )
                )
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.showVitalSignsForm(identifierVitalSigns)
        viewModel.updateVitalSignsInfoCard(mapOf(TAS_KEY to "10", FC_KEY to "10"))

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when showMedicineForm is called`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.showMedicineForm(IDENTIFIER)

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isMedsSelectorEvent)
    }

    @Test
    fun `when updateMedicineInfoCard is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                segmentedSwitchUiModelMock,
                medsSelectorUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.showMedicineForm(IDENTIFIER)
        viewModel.updateMedicineInfoCard(mapOf(IDENTIFIER to IDENTIFIER))

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when showSignaturePad is called`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.showSignaturePad(IDENTIFIER)

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isSignatureEvent)
    }

    @Test
    fun `when updateSignature is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                segmentedSwitchUiModelMock,
                signatureUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.showSignaturePad(IDENTIFIER)
        viewModel.updateSignature("signature")

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when sendMedicalHistory is required textField without data`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = ""),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true)
                )
            )
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        coVerify(exactly = 0) {
            sendMedicalHistory.invoke(
                idAph = any(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        }
    }

    @Test
    fun `when sendMedicalHistory is required chip options with selected null`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true, selected = null),
                    dropDownUiModelMock.copy(required = true)
                )
            )
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        coVerify(exactly = 0) {
            sendMedicalHistory.invoke(
                idAph = any(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        }
    }

    @Test
    fun `when sendMedicalHistory is required chip selection with selected false`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(
                        required = true,
                        items = chipOptionsUiModelMock.items.map { it.copy(selected = false) }
                    ),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true)
                )
            )
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        coVerify(exactly = 0) {
            sendMedicalHistory.invoke(
                idAph = any(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        }
    }

    @Test
    fun `when sendMedicalHistory is required dropdown without selected data`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(
                        required = true,
                        items = chipOptionsUiModelMock.items.map { it.copy(selected = false) }
                    ),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true, selected = "")
                )
            )
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        coVerify(exactly = 0) {
            sendMedicalHistory.invoke(
                idAph = any(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        }
    }

    @Test
    fun `when sendMedicalHistory is not required textField with data`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = false, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true),
                    signatureUiModelMock.copy(required = true),
                    imageButtonSectionUiModelMock.copy(required = true, selected = "selected")
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                idAph = ID_APH.toString(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        } returns Result.success(Unit)

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(MEDICAL_APH_BANNER_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendMedicalHistory is not required chip option`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = false),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true),
                    signatureUiModelMock.copy(required = true),
                    imageButtonSectionUiModelMock.copy(required = true, selected = "selected")
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                idAph = ID_APH.toString(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        } returns Result.success(Unit)

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(MEDICAL_APH_BANNER_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendMedicalHistory is not required chip selection`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = false),
                    dropDownUiModelMock.copy(required = true),
                    signatureUiModelMock.copy(required = true),
                    imageButtonSectionUiModelMock.copy(required = true, selected = "selected")
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                idAph = ID_APH.toString(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        } returns Result.success(Unit)

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(MEDICAL_APH_BANNER_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendMedicalHistory is not required dropdown`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = false),
                    signatureUiModelMock.copy(required = true),
                    imageButtonSectionUiModelMock.copy(required = true, selected = "selected")
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                idAph = ID_APH.toString(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        } returns Result.success(Unit)

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(MEDICAL_APH_BANNER_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendMedicalHistory is not required signature`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true),
                    signatureUiModelMock.copy(required = false),
                    imageButtonSectionUiModelMock.copy(required = true, selected = "selected")
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                idAph = ID_APH.toString(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        } returns Result.success(Unit)

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(MEDICAL_APH_BANNER_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendMedicalHistory is not required imageButtonSection`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true),
                    signatureUiModelMock.copy(required = true),
                    imageButtonSectionUiModelMock.copy(required = false)
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                idAph = ID_APH.toString(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        } returns Result.success(Unit)

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(MEDICAL_APH_BANNER_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendMedicalHistory is success`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true),
                    signatureUiModelMock.copy(required = true),
                    imageButtonSectionUiModelMock.copy(required = true, selected = "selected")
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                idAph = ID_APH.toString(),
                humanBodyValues = any(),
                segmentedValues = any(),
                signatureValues = any(),
                fieldsValue = any(),
                sliderValues = any(),
                dropDownValues = any(),
                chipSelectionValues = any(),
                chipOptionsValues = any(),
                imageButtonSectionValues = any(),
                vitalSigns = any(),
                infoCardButtonValues = any(),
                images = any()
            )
        } returns Result.success(Unit)

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(MEDICAL_APH_BANNER_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendMedicalHistory is failure`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            ScreenModel(
                body = listOf(
                    textFieldUiModelMock.copy(required = true, text = "text"),
                    chipOptionsUiModelMock.copy(required = true),
                    chipSelectionUiModelMock.copy(required = true),
                    dropDownUiModelMock.copy(required = true),
                    signatureUiModelMock.copy(required = true),
                    imageButtonSectionUiModelMock.copy(required = true, selected = "selected")
                )
            )
        )
        coEvery {
            sendMedicalHistory.invoke(
                ID_APH.toString(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.failure(Throwable())

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.sendMedicalHistory(listOf())

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorEvent?.title)
    }

    @Test
    fun `when showCamera is called`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.showCamera()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.showCamera)
    }

    @Test
    fun `when onPhotoTaken is called`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.onPhotoTaken(mockk())

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.photoTaken)
        Assert.assertNotNull(viewModel.uiState.selectedMediaUris)
    }

    @Test
    fun `when updateMediaActions is called without parameter`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                segmentedSwitchUiModelMock,
                mediaActionsUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.updateMediaActions()

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when updateMediaActions is called with parameter`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                segmentedSwitchUiModelMock,
                mediaActionsUiModelMock
            )
        )
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.updateMediaActions(listOf(mockk()))

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when call handleEvent uiState should have errorModel clear`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.errorEvent)
    }

    @Test
    fun `when call consumeShownInfoEvent uiState should have infoEvent clear`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.consumeShownInfoEvent()

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when call consumeNavigationEvent uiState should have navigationModel clear`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when call goBack uiState should have navigationModel back true`() = runTest {
        coEvery { getMedicalHistoryScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = MedicalHistoryViewModel(
            savedStateHandle = savedStateHandle,
            getMedicalHistoryScreen = getMedicalHistoryScreen,
            logoutCurrentUser = logoutCurrentUser,
            sendMedicalHistory = sendMedicalHistory,
            observeOperationConfig = observeOperationConfig,
            stringProvider = stringProvider
        )
        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }
}
