package com.skgtecnologia.sisem.ui.authcards.create

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.authcards.usecases.GetAuthCardsScreen
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.operation.usecases.GetOperationConfig
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
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

private const val ANDROID_ID = "123"
private const val SERVER_ERROR_TITLE = "Error en servidor"

class AuthCardsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var getAuthCardsScreen: GetAuthCardsScreen

    @MockK
    private lateinit var getOperationConfig: GetOperationConfig

    private val screenModel = ScreenModel(body = emptyList())

    private lateinit var authCardsViewModel: AuthCardsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when init view model operation config is success`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(screenModel)

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        Assert.assertEquals(screenModel, authCardsViewModel.uiState.screenModel)
    }

    @Test
    fun `when init view model operation config fails`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.failure(IllegalStateException())

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        Assert.assertEquals(SERVER_ERROR_TITLE, authCardsViewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when init view model getAuthCardsScreen fails`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.failure(IllegalStateException())

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        Assert.assertEquals(SERVER_ERROR_TITLE, authCardsViewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when call showReportBottomSheet get reportDetail`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(screenModel)
        val reportDetail = mockk<ReportsDetailUiModel>()

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        authCardsViewModel.showReportBottomSheet(reportDetail)

        Assert.assertEquals(reportDetail, authCardsViewModel.uiState.reportDetail)
    }

    @Test
    fun `when call handleShownReportBottomSheet clear reportDetail`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(screenModel)

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        authCardsViewModel.handleShownReportBottomSheet()

        Assert.assertEquals(null, authCardsViewModel.uiState.reportDetail)
    }

    @Test
    fun `when call showFindingsBottomSheet get chipSection`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(screenModel)
        val chipSection = mockk<ChipSectionUiModel>()

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        authCardsViewModel.showFindingsBottomSheet(chipSection)

        Assert.assertEquals(chipSection, authCardsViewModel.uiState.chipSection)
    }

    @Test
    fun `when call handleShownFindingsBottomSheet clear chipSection`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(screenModel)

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        authCardsViewModel.handleShownFindingsBottomSheet()

        Assert.assertEquals(null, authCardsViewModel.uiState.chipSection)
    }

    @Test
    fun `when call handleShownError clear reportDetail`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(screenModel)

        authCardsViewModel =
            AuthCardsViewModel(androidIdProvider, getAuthCardsScreen, getOperationConfig)

        authCardsViewModel.handleShownError()

        Assert.assertEquals(null, authCardsViewModel.uiState.errorModel)
    }
}
