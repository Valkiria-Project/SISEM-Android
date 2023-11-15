package com.skgtecnologia.sisem.ui.medicalhistory.medicine

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.BuildMedicineInformation
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicineScreen
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
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

    private lateinit var viewModel: MedicineViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getMedicineScreen is success`() = runTest {
        coEvery { getMedicineScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = MedicineViewModel(buildMedicineInformation, getMedicineScreen)

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getMedicineScreen is failure`() = runTest {
        coEvery { getMedicineScreen.invoke() } returns Result.failure(Throwable())

        viewModel = MedicineViewModel(buildMedicineInformation, getMedicineScreen)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when saveMedicine is called`() = runTest {
        val identifier = "identifier"
        val identifier2 = "identifier2"
        val values = emptyMap<String, String>()
        coEvery { getMedicineScreen.invoke() } returns Result.success(emptyScreenModel)
        every { buildMedicineInformation.invoke(any(), any(), any(), any()) } returns values

        viewModel = MedicineViewModel(buildMedicineInformation, getMedicineScreen)
        viewModel.fieldsValues[identifier] = InputUiModel(
            identifier = identifier,
            updatedValue = identifier,
            fieldValidated = true
        )
        viewModel.dropDownValue.value = DropDownInputUiModel(
            identifier = identifier,
            id = identifier,
            name = identifier,
            fieldValidated = true
        )
        viewModel.chipValues[identifier] = ChipSelectionItemUiModel(
            id = identifier,
            name = identifier,
        )
        viewModel.chipValues[identifier2] = ChipSelectionItemUiModel(
            id = identifier2,
            name = identifier2,
        )
        viewModel.saveMedicine()

        Assert.assertEquals(values, viewModel.uiState.navigationModel?.values)
    }

    @Test
    fun `when consumeInfoEvent is called`() = runTest {
        coEvery { getMedicineScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = MedicineViewModel(buildMedicineInformation, getMedicineScreen)
        viewModel.consumeInfoEvent()

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        coEvery { getMedicineScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = MedicineViewModel(buildMedicineInformation, getMedicineScreen)
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when navigateBack is called`() = runTest {
        coEvery { getMedicineScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = MedicineViewModel(buildMedicineInformation, getMedicineScreen)
        viewModel.navigateBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.goBack)
    }
}
