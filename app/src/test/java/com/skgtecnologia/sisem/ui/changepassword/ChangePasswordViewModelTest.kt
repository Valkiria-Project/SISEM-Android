package com.skgtecnologia.sisem.ui.changepassword

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.domain.changepassword.usecases.ChangePassword
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetChangePasswordScreen
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetLoginNavigationModel
import com.skgtecnologia.sisem.domain.changepassword.usecases.OnCancel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.login.LoginNavigationModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val EMPTY_FIELDS_DESCRIPTION =
    "Para guardar el cambio de contraseña es necesario que complete todos los campos."
private const val NO_MATCH_DESCRIPTION =
    "Nueva contraseña y confirmar nueva contraseña no coinciden."
private const val PASSWORD_1 = "123456"
private const val PASSWORD_2 = "1234567"

class ChangePasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var getChangePasswordScreen: GetChangePasswordScreen

    @MockK
    lateinit var getLoginNavigationModel: GetLoginNavigationModel

    @MockK
    lateinit var onCancel: OnCancel

    @MockK
    lateinit var changePassword: ChangePassword

    private val screenModel = ScreenModel(body = emptyList())

    lateinit var viewModel: ChangePasswordViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when init view model and getChangePasswordScreen is success`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        Assert.assertEquals(screenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when init view model and getChangePasswordScreen is failure`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.failure(Throwable())

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when changePassword and old password is empty`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.change()

        Assert.assertEquals(EMPTY_FIELDS_DESCRIPTION, viewModel.uiState.errorModel?.description)
    }

    @Test
    fun `when changePassword and newPassword is empty`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.change()

        Assert.assertEquals(EMPTY_FIELDS_DESCRIPTION, viewModel.uiState.errorModel?.description)
    }

    @Test
    fun `when changePassword and confirmedNewPassword is empty`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.change()

        Assert.assertEquals(EMPTY_FIELDS_DESCRIPTION, viewModel.uiState.errorModel?.description)
    }

    @Test
    fun `when changePassword and newPassword and confirmedNewPassword are different`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.confirmedNewPassword = PASSWORD_2
        viewModel.change()

        Assert.assertEquals(NO_MATCH_DESCRIPTION, viewModel.uiState.errorModel?.description)
    }

    @Test
    fun `when changePassword and isValidNewPassword is false`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.confirmedNewPassword = PASSWORD_1
        viewModel.isValidNewPassword = false
        viewModel.isValidConfirmedNewPassword = true
        viewModel.change()

        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when changePassword and isValidConfirmedNewPassword is false`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.confirmedNewPassword = PASSWORD_1
        viewModel.isValidNewPassword = true
        viewModel.isValidConfirmedNewPassword = false
        viewModel.change()

        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when changePassword and validators are false`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.confirmedNewPassword = PASSWORD_1
        viewModel.isValidNewPassword = false
        viewModel.isValidConfirmedNewPassword = false
        viewModel.change()

        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when change, validators are true and changePassword is failure`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)
        coEvery { changePassword.invoke(any(), any()) } returns Result.failure(Throwable())

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.confirmedNewPassword = PASSWORD_1
        viewModel.isValidNewPassword = true
        viewModel.isValidConfirmedNewPassword = true
        viewModel.change()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when change and getLoginNavigationModel is failure`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)
        coEvery { changePassword.invoke(any(), any()) } returns Result.success("")
        coEvery { getLoginNavigationModel.invoke() } returns Result.failure(Throwable())

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.confirmedNewPassword = PASSWORD_1
        viewModel.isValidNewPassword = true
        viewModel.isValidConfirmedNewPassword = true
        viewModel.change()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when change and getLoginNavigationModel is success`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)
        coEvery { changePassword.invoke(any(), any()) } returns Result.success("")
        val navigationModel = mockk<LoginNavigationModel>(relaxed = true)
        coEvery { getLoginNavigationModel.invoke() } returns Result.success(navigationModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.oldPassword = PASSWORD_1
        viewModel.newPassword = PASSWORD_1
        viewModel.confirmedNewPassword = PASSWORD_1
        viewModel.isValidNewPassword = true
        viewModel.isValidConfirmedNewPassword = true
        viewModel.change()

        Assert.assertEquals(navigationModel, viewModel.uiState.loginNavigationModel)
    }

    @Test
    fun `when onChangePasswordHandled is called clear uiState and resetForm`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.onChangePasswordHandled()

        Assert.assertEquals(false, viewModel.uiState.validateFields)
        Assert.assertEquals(null, viewModel.uiState.successInfoModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
        Assert.assertEquals("", viewModel.oldPassword)
        Assert.assertEquals("", viewModel.newPassword)
        Assert.assertEquals("", viewModel.confirmedNewPassword)
        Assert.assertEquals(false, viewModel.isValidNewPassword)
        Assert.assertEquals(false, viewModel.isValidConfirmedNewPassword)
    }

    @Test
    fun `when handleShownError is called clear errorModel`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.handleShownError()

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }

    @Test
    fun `when cancel is called and failure`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)
        coEvery { onCancel.invoke() } returns Result.failure(Throwable())

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.cancel()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when cancel is called and success`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)
        coEvery { onCancel.invoke() } returns Result.success(Unit)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.cancel()

        Assert.assertEquals(true, viewModel.uiState.onCancel)
    }

    @Test
    fun `when onCancelHandled is called reset onCancel`() = runTest {
        coEvery { getChangePasswordScreen.invoke() } returns Result.success(screenModel)

        viewModel = ChangePasswordViewModel(
            getChangePasswordScreen,
            getLoginNavigationModel,
            onCancel,
            changePassword
        )

        viewModel.onCancelHandled()

        Assert.assertEquals(false, viewModel.uiState.onCancel)
    }
}
