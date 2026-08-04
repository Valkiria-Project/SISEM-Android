package com.skgtecnologia.sisem.ui.humanbody.wounds

import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val BURN = "Quemadura"
private const val BRUISE = "Contusión"
private const val CUT = "Herida cortante"
private const val FIRST_DEGREE = "Primer grado"
private const val SECOND_DEGREE = "Segundo grado"

class WoundsViewModelTest {

    private lateinit var viewModel: WoundsViewModel

    @Before
    fun setup() {
        viewModel = WoundsViewModel()
        viewModel.setBurnList(listOf(FIRST_DEGREE, SECOND_DEGREE))
    }

    @Test
    fun `selecting a wound adds it to the selection`() {
        viewModel.updateWoundsList(BRUISE, isSelected = false)

        Assert.assertEquals(listOf(BRUISE), viewModel.uiState.selectedWounds)
        Assert.assertEquals(false, viewModel.uiState.onBurnSelected)
    }

    @Test
    fun `selecting the same wound twice takes it back out`() {
        viewModel.updateWoundsList(BRUISE, isSelected = false)
        viewModel.updateWoundsList(BRUISE, isSelected = false)

        Assert.assertEquals(emptyList<String>(), viewModel.uiState.selectedWounds)
    }

    @Test
    fun `wounds accumulate independently of each other`() {
        viewModel.updateWoundsList(BRUISE, isSelected = false)
        viewModel.updateWoundsList(CUT, isSelected = false)

        Assert.assertEquals(listOf(BRUISE, CUT), viewModel.uiState.selectedWounds)
    }

    @Test
    fun `choosing burn opens the degree picker without recording burn itself`() {
        viewModel.updateWoundsList(BURN, isSelected = true)

        // Burn is a category, not a diagnosis: the flag tells the screen to ask which
        // degree, and only that answer ends up in the selection.
        Assert.assertEquals(true, viewModel.uiState.onBurnSelected)
        Assert.assertEquals(false, viewModel.uiState.selectedWounds.contains(BURN))
    }

    @Test
    fun `picking a degree records it`() {
        viewModel.updateWoundsList(BURN, isSelected = true)
        viewModel.updateBurnList(SECOND_DEGREE)

        Assert.assertEquals(listOf(SECOND_DEGREE), viewModel.uiState.selectedWounds)
    }

    @Test
    fun `changing the degree replaces the previous one instead of piling up`() {
        viewModel.updateWoundsList(BURN, isSelected = true)
        viewModel.updateBurnList(FIRST_DEGREE)
        viewModel.updateBurnList(SECOND_DEGREE)

        Assert.assertEquals(listOf(SECOND_DEGREE), viewModel.uiState.selectedWounds)
    }

    @Test
    fun `unchecking burn clears the degree but leaves the other wounds alone`() {
        viewModel.updateWoundsList(BRUISE, isSelected = false)
        viewModel.updateWoundsList(BURN, isSelected = true)
        viewModel.updateBurnList(SECOND_DEGREE)

        viewModel.updateWoundsList(BURN, isSelected = false)

        Assert.assertEquals(listOf(BRUISE), viewModel.uiState.selectedWounds)
        Assert.assertEquals(false, viewModel.uiState.onBurnSelected)
    }

    @Test
    fun `handling the selection resets both the list and the burn flag`() {
        viewModel.updateWoundsList(BRUISE, isSelected = false)
        viewModel.updateWoundsList(BURN, isSelected = true)

        viewModel.onSelectedWoundsHandled()

        Assert.assertEquals(emptyList<String>(), viewModel.uiState.selectedWounds)
        Assert.assertEquals(false, viewModel.uiState.onBurnSelected)
    }

    @Test
    fun `the burn flag survives selecting another wound afterwards`() {
        viewModel.updateWoundsList(BURN, isSelected = true)

        viewModel.updateWoundsList(CUT, isSelected = false)

        // Adding an unrelated wound must not close the degree picker that is still open.
        Assert.assertEquals(true, viewModel.uiState.onBurnSelected)
        Assert.assertEquals(listOf(CUT), viewModel.uiState.selectedWounds)
    }
}
