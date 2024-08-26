package com.skgtecnologia.sisem.ui.forgotpassword

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.domain.forgotpassword.usecases.GetForgotPasswordScreen
import com.skgtecnologia.sisem.domain.forgotpassword.usecases.SendEmail
import com.valkiria.uicomponents.components.textfield.InputUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val BANNER_TITLE = "Correo enviado"

class ForgotPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var getForgotPasswordScreen: GetForgotPasswordScreen

    @MockK
    lateinit var sendEmail: SendEmail

    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getForgotPasswordScreen is success`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.initScreen()

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getForgotPasswordScreen is failure`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.failure(Throwable())

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.initScreen()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when sendEmail is success`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.success(emptyScreenModel)
        coEvery { sendEmail.invoke(any()) } returns Result.success(Unit)

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.emailValue.value = InputUiModel(
            identifier = "test",
            updatedValue = "test",
            fieldValidated = true
        )
        viewModel.sendEmail()

        Assert.assertEquals(BANNER_TITLE, viewModel.uiState.successBanner?.title)
    }

    @Test
    fun `when sendEmail is failure`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.success(emptyScreenModel)
        coEvery { sendEmail.invoke(any()) } returns Result.failure(Throwable())

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.emailValue.value = InputUiModel(
            identifier = "test",
            updatedValue = "test",
            fieldValidated = true
        )
        viewModel.sendEmail()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when consumeNavigationEvent is called clear uiState`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.successBanner)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.validateFields)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when handleShownError is called clear errorModel`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.consumeErrorEvent()

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }

    @Test
    fun `when consumeSuccessEvent is called clear successBanner`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.consumeSuccessEvent()

        Assert.assertEquals(null, viewModel.uiState.successBanner)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isSuccess)
    }

    @Test
    fun `when cancel is called navigationModel isCancel is true`() = runTest {
        coEvery { getForgotPasswordScreen.invoke() } returns Result.success(emptyScreenModel)

        viewModel = ForgotPasswordViewModel(getForgotPasswordScreen, sendEmail)
        viewModel.cancel()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isCancel)
    }
}
