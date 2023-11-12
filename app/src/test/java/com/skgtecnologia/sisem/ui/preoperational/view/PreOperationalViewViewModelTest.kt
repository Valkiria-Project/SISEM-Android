package com.skgtecnologia.sisem.ui.preoperational.view

import androidx.lifecycle.SavedStateHandle
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreenView
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.ROLE
import com.valkiria.uicomponents.action.GenericUiAction
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

class PreOperationalViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getPreOperationalScreenView: GetPreOperationalScreenView

    private val savedStateHandle =
        SavedStateHandle(mapOf(ROLE to OperationRole.MEDIC_APH.name))

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
        viewModel = PreOperationalViewViewModel(
            SavedStateHandle(mapOf(ROLE to null)),
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

    // creat test para el metodo handleEvent
    @Test
    fun `when handleEvent is called`() = runTest {
        coEvery { getPreOperationalScreenView.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewViewModel(
            savedStateHandle,
            androidIdProvider,
            logoutCurrentUser,
            getPreOperationalScreenView
        )

        viewModel.handleEvent(GenericUiAction.DismissAction)

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }
}
