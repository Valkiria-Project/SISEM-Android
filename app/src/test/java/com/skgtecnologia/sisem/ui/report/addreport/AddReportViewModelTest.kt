package com.skgtecnologia.sisem.ui.report.addreport

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.report.usecases.GetAddReportScreen
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddReportViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getAddReportScreen: GetAddReportScreen

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    private lateinit var viewModel: AddReportViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getAddReportScreen is success`() = runTest {
        coEvery { getAddReportScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = AddReportViewModel(getAddReportScreen, logoutCurrentUser)

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getAddReportScreen is failure`() = runTest {
        coEvery { getAddReportScreen.invoke() } returns Result.failure(Throwable())

        viewModel = AddReportViewModel(getAddReportScreen, logoutCurrentUser)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when handleEvent is called`() = runTest {
        coEvery { getAddReportScreen.invoke() } returns Result.failure(Throwable())
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = AddReportViewModel(getAddReportScreen, logoutCurrentUser)
        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }
}
