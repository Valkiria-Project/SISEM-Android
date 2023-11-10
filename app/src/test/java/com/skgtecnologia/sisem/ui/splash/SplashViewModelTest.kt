package com.skgtecnologia.sisem.ui.splash

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.domain.operation.usecases.GetStartupState
import com.skgtecnologia.sisem.ui.navigation.StartupNavigationModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getStartupState: GetStartupState

    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when StartupNavigationModel is Admin and has vehicleCode`() = runTest {
        val startupNavigationModel = StartupNavigationModel(
            isAdmin = true,
            vehicleCode = "123"
        )

        coEvery { getStartupState.invoke() } returns Result.success(startupNavigationModel)

        splashViewModel = SplashViewModel(getStartupState)

        Assert.assertEquals(
            startupNavigationModel.isAdmin,
            splashViewModel.uiState.value.startupNavigationModel?.isAdmin
        )
        Assert.assertEquals(
            startupNavigationModel.vehicleCode,
            splashViewModel.uiState.value.startupNavigationModel?.vehicleCode
        )
    }
}
