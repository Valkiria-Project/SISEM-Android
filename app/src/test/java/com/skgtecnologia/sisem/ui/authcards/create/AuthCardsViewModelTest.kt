package com.skgtecnologia.sisem.ui.authcards.create

import androidx.lifecycle.SavedStateHandle
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.biometric.FaceCredentialStore
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.authcards.usecases.GetAuthCardsScreen
import com.skgtecnologia.sisem.domain.operation.usecases.GetOperationConfig
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AuthCardsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var getAuthCardsScreen: GetAuthCardsScreen

    @MockK
    private lateinit var getOperationConfig: GetOperationConfig

    @MockK
    private lateinit var faceCredentialStore: FaceCredentialStore

    private val savedStateHandle = SavedStateHandle().apply {
        set("loggedOutRole", null)
    }

    private lateinit var authCardsViewModel: AuthCardsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
        coEvery { faceCredentialStore.enrolledUsernames() } returns emptyList()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when call init and operation config is success`() = runTest {
        coEvery { getOperationConfig.invoke(ANDROID_ID) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)

        authCardsViewModel = AuthCardsViewModel(
            androidIdProvider,
            getAuthCardsScreen,
            getOperationConfig,
            faceCredentialStore,
            savedStateHandle
        )

        Assert.assertEquals(emptyScreenModel, authCardsViewModel.uiState.screenModel)
    }

    @Test
    fun `when call init and operation config fails`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.failure(Throwable())

        authCardsViewModel = AuthCardsViewModel(
            androidIdProvider,
            getAuthCardsScreen,
            getOperationConfig,
            faceCredentialStore,
            savedStateHandle
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, authCardsViewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when call init and getAuthCardsScreen fails`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.failure(Throwable())

        authCardsViewModel = AuthCardsViewModel(
            androidIdProvider,
            getAuthCardsScreen,
            getOperationConfig,
            faceCredentialStore,
            savedStateHandle
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, authCardsViewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when call showReportBottomSheet uiState should have reportDetail`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(emptyScreenModel)
        val reportDetail = mockk<ReportsDetailUiModel>()

        authCardsViewModel = AuthCardsViewModel(
            androidIdProvider,
            getAuthCardsScreen,
            getOperationConfig,
            faceCredentialStore,
            savedStateHandle
        )
        authCardsViewModel.showReportBottomSheet(reportDetail)

        Assert.assertEquals(reportDetail, authCardsViewModel.uiState.reportDetail)
    }

    @Test
    fun `when call consumeReportBottomSheetEvent uiState should have reportDetail clear`() =
        runTest {
            coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
            coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(emptyScreenModel)

            authCardsViewModel = AuthCardsViewModel(
                androidIdProvider,
                getAuthCardsScreen,
                getOperationConfig,
                faceCredentialStore,
                savedStateHandle
            )
            authCardsViewModel.consumeReportBottomSheetEvent()

            Assert.assertEquals(null, authCardsViewModel.uiState.reportDetail)
        }

    @Test
    fun `when call showFindingsBottomSheet uiState should have chipSection`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(emptyScreenModel)
        val chipSection = mockk<ChipSectionUiModel>()

        authCardsViewModel = AuthCardsViewModel(
            androidIdProvider,
            getAuthCardsScreen,
            getOperationConfig,
            faceCredentialStore,
            savedStateHandle
        )
        authCardsViewModel.showFindingsBottomSheet(chipSection)

        Assert.assertEquals(chipSection, authCardsViewModel.uiState.chipSection)
    }

    @Test
    fun `when call consumeFindingsBottomSheetEvent uiState should have chipSection clear`() =
        runTest {
            coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
            coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(emptyScreenModel)

            authCardsViewModel = AuthCardsViewModel(
                androidIdProvider,
                getAuthCardsScreen,
                getOperationConfig,
                faceCredentialStore,
                savedStateHandle
            )
            authCardsViewModel.consumeFindingsBottomSheetEvent()

            Assert.assertEquals(null, authCardsViewModel.uiState.chipSection)
        }

    @Test
    fun `when call consumeErrorEvent uiState should have errorModel clear`() = runTest {
        coEvery { getOperationConfig.invoke(any()) } returns Result.success(mockk())
        coEvery { getAuthCardsScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        authCardsViewModel = AuthCardsViewModel(
            androidIdProvider,
            getAuthCardsScreen,
            getOperationConfig,
            faceCredentialStore,
            savedStateHandle
        )
        authCardsViewModel.consumeErrorEvent()

        Assert.assertEquals(null, authCardsViewModel.uiState.errorModel)
    }
}
