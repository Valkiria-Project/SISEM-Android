package com.skgtecnologia.sisem.ui.medicalhistory.vitalsings

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.textFieldUiModelMock
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetVitalSignsScreen
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VitalSignsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getVitalSignsScreen: GetVitalSignsScreen

    private lateinit var viewModel: VitalSignsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getVitalSignsScreen is success`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(textFieldUiModelMock)
        )
        coEvery { getVitalSignsScreen.invoke() } returns Result.success(screenModel)

        viewModel = VitalSignsViewModel(getVitalSignsScreen)

        Assert.assertEquals(screenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getVitalSignsScreen is failure`() = runTest {
        val throwable = Throwable("Error")
        coEvery { getVitalSignsScreen.invoke() } returns Result.failure(throwable)

        viewModel = VitalSignsViewModel(getVitalSignsScreen)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when saveVitalSigns is called`() = runTest {
        val identifier = "identifier"
        coEvery { getVitalSignsScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = VitalSignsViewModel(getVitalSignsScreen)
        viewModel.fieldsValues[identifier] = InputUiModel(
            identifier = identifier,
            updatedValue = identifier,
            fieldValidated = true
        )
        viewModel.saveVitalSigns()

        Assert.assertNotNull(viewModel.uiState.navigationModel?.values)
    }

    @Test
    fun `when consumeInfoEvent is called`() = runTest {
        coEvery { getVitalSignsScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = VitalSignsViewModel(getVitalSignsScreen)
        viewModel.consumeInfoEvent()

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        coEvery { getVitalSignsScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = VitalSignsViewModel(getVitalSignsScreen)
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when navigateBack is called`() = runTest {
        coEvery { getVitalSignsScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = VitalSignsViewModel(getVitalSignsScreen)
        viewModel.navigateBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.goBack)
    }
}
