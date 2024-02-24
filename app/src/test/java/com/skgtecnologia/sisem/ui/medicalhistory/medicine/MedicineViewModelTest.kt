package com.skgtecnologia.sisem.ui.medicalhistory.medicine

import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.BuildMedicineInformation
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicineScreen
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MedicineViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var buildMedicineInformation: BuildMedicineInformation

    @MockK
    private lateinit var getMedicineScreen: GetMedicineScreen

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    private lateinit var viewModel: MedicineViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when getMedicineScreen is success`() = runTest {
        coEvery { getMedicineScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel =
            MedicineViewModel(buildMedicineInformation, getMedicineScreen, androidIdProvider)

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getMedicineScreen is failure`() = runTest {
        coEvery { getMedicineScreen.invoke(any()) } returns Result.failure(Throwable())

        viewModel =
            MedicineViewModel(buildMedicineInformation, getMedicineScreen, androidIdProvider)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when saveMedicine is called`() = runTest {
        val identifier = "identifier"
        val identifier2 = "identifier2"
        val values = emptyMap<String, String>()
        coEvery { getMedicineScreen.invoke(any()) } returns Result.success(emptyScreenModel)
        every { buildMedicineInformation.invoke(any(), any(), any(), any()) } returns values

        viewModel =
            MedicineViewModel(buildMedicineInformation, getMedicineScreen, androidIdProvider)
        val inputAction = GenericUiAction.InputAction(
            identifier = identifier,
            updatedValue = identifier,
            fieldValidated = true,
            required = false
        )
        viewModel.handleInputAction(inputAction)
        val dropDownAction = GenericUiAction.DropDownAction(
            identifier = identifier,
            id = identifier,
            name = identifier,
            quantity = 2,
            fieldValidated = true,
        )
        viewModel.handleDropDownAction(dropDownAction)
        val chipSelectionAction = GenericUiAction.ChipSelectionAction(
            identifier = identifier,
            chipSelectionItemUiModel = ChipSelectionItemUiModel(identifier2, identifier2),
            status = true,
            viewsVisibility = mapOf()
        )
        viewModel.handleChipSelectionAction(chipSelectionAction)
        val chipSelectionAction2 = GenericUiAction.ChipSelectionAction(
            identifier = identifier2,
            chipSelectionItemUiModel = ChipSelectionItemUiModel(identifier, identifier),
            status = true,
            viewsVisibility = mapOf()
        )
        viewModel.handleChipSelectionAction(chipSelectionAction2)
        viewModel.saveMedicine()

        Assert.assertEquals(values, viewModel.uiState.navigationModel?.values)
    }

    @Test
    fun `when consumeInfoEvent is called`() = runTest {
        coEvery { getMedicineScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel =
            MedicineViewModel(buildMedicineInformation, getMedicineScreen, androidIdProvider)
        viewModel.consumeInfoEvent()

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        coEvery { getMedicineScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel =
            MedicineViewModel(buildMedicineInformation, getMedicineScreen, androidIdProvider)
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when navigateBack is called`() = runTest {
        coEvery { getMedicineScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel =
            MedicineViewModel(buildMedicineInformation, getMedicineScreen, androidIdProvider)
        viewModel.navigateBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.goBack)
    }
}
