package com.skgtecnologia.sisem.ui.preoperational.view

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.DRIVER_ROLE
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreenView
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PreOperationalViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getPreOperationalScreenView: GetPreOperationalScreenView

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = MainRoute.PreoperationalViewRoute(role = DRIVER_ROLE)
    )

    private lateinit var viewModel: PreOperationalViewViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when getPreOperationalScreenView is success`() = runTest {
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getPreOperationalScreenView is failure`() = runTest {
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.failure(
            Throwable()
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when role is null`() = runTest {
        val savedStateHandle = SavedStateHandle(
            route = MainRoute.PreoperationalViewRoute(role = "")
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when goBack is called`() = runTest {
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )
        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when handleShownFindingBottomSheet is called`() = runTest {
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )
        viewModel.handleShownFindingBottomSheet()

        Assert.assertEquals(null, viewModel.uiState.findingDetail)
    }

    @Test
    fun `when showFindings is called`() = runTest {
        val findingDetail = mockk<FindingsDetailUiModel>()
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )
        viewModel.showFindings(findingDetail)

        Assert.assertEquals(findingDetail, viewModel.uiState.findingDetail)
    }

    @Test
    fun `when handleEvent is called`() = runTest {
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )
        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }
}
