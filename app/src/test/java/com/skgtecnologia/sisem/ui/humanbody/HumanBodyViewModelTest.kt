package com.skgtecnologia.sisem.ui.humanbody

import com.skgtecnologia.sisem.ui.humanbody.area.BackArea
import com.skgtecnologia.sisem.ui.humanbody.area.FrontArea
import io.mockk.MockKAnnotations
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HumanBodyViewModelTest {

    private lateinit var viewModel: HumanBodyViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = HumanBodyViewModel()
    }

    @Test
    fun `when saveFrontList`() {
        viewModel.saveFrontList(FrontArea.ABDOMEN)

        Assert.assertEquals(
            listOf(FrontArea.ABDOMEN),
            viewModel.uiState.selectedFrontAreas
        )
    }

    @Test
    fun `updateFrontList with non selectedArea should be true onSelectWound`() {
        viewModel.saveFrontList(FrontArea.ABDOMEN)
        viewModel.updateFrontList(FrontArea.CHEST)

        Assert.assertEquals(
            listOf(FrontArea.ABDOMEN),
            viewModel.uiState.selectedFrontAreas
        )
        Assert.assertEquals(1, viewModel.uiState.selectedFrontAreas.size)
        Assert.assertEquals(true, viewModel.uiState.onSelectWound)
    }

    @Test
    fun `updateFrontList with selectedArea should be remove area and false onSelectedWounds`() {
        viewModel.saveFrontList(FrontArea.ABDOMEN)
        viewModel.updateFrontList(FrontArea.ABDOMEN)

        Assert.assertEquals(
            listOf<FrontArea>(),
            viewModel.uiState.selectedFrontAreas
        )
        Assert.assertEquals(0, viewModel.uiState.selectedFrontAreas.size)
        Assert.assertEquals(false, viewModel.uiState.onSelectWound)
    }

    @Test
    fun `when saveBackList`() {
        viewModel.saveBackList(BackArea.LOWER_BACK)

        Assert.assertEquals(
            listOf(BackArea.LOWER_BACK),
            viewModel.uiState.selectedBackAreas
        )
    }

    @Test
    fun `updateBackList with non selectedArea should be true onSelectWound`() {
        viewModel.saveBackList(BackArea.LOWER_BACK)
        viewModel.updateBackList(BackArea.BACK)

        Assert.assertEquals(
            listOf(BackArea.LOWER_BACK),
            viewModel.uiState.selectedBackAreas
        )
        Assert.assertEquals(1, viewModel.uiState.selectedBackAreas.size)
        Assert.assertEquals(true, viewModel.uiState.onSelectWound)
    }

    @Test
    fun `updateBackList with selectedArea should be remove area and false onSelectedWounds`() {
        viewModel.saveBackList(BackArea.LOWER_BACK)
        viewModel.updateBackList(BackArea.LOWER_BACK)

        Assert.assertEquals(
            listOf<BackArea>(),
            viewModel.uiState.selectedBackAreas
        )
        Assert.assertEquals(0, viewModel.uiState.selectedFrontAreas.size)
        Assert.assertEquals(false, viewModel.uiState.onSelectWound)
    }

    @Test
    fun `consumeWoundSelectedEvent should be false onSelectWound`() {
        viewModel.consumeWoundSelectedEvent()

        Assert.assertEquals(false, viewModel.uiState.onSelectWound)
    }
}
