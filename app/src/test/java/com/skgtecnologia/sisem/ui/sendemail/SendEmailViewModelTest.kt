package com.skgtecnologia.sisem.ui.sendemail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.sendemail.usecases.SendEmail
import com.skgtecnologia.sisem.ui.navigation.AphRoute
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.components.textfield.InputUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

private const val ID_APH = "APH-991"
private const val RECIPIENT = "medico@sisem.gov.co"
private const val BODY = "Adjunto la historia clínica."

@RunWith(RobolectricTestRunner::class)
class SendEmailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var sendEmail: SendEmail

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = AphRoute.SendEmailRoute(idAph = ID_APH)
    )

    private lateinit var viewModel: SendEmailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        every { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) } just runs

        viewModel = SendEmailViewModel(savedStateHandle, sendEmail, logoutCurrentUser)
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    private fun withRecipient(validated: Boolean) {
        viewModel.emailValue.value = InputUiModel(
            identifier = "EMAIL",
            updatedValue = RECIPIENT,
            fieldValidated = validated
        )
        viewModel.bodyValue.value = BODY
    }

    @Test
    fun `an unvalidated address is never sent`() = runTest {
        withRecipient(validated = false)

        viewModel.sendEmail()

        Assert.assertEquals(true, viewModel.uiState.validateFields)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
        coVerify(exactly = 0) { sendEmail.invoke(any(), any(), any()) }
    }

    @Test
    fun `the email goes out against the record the screen was opened for`() = runTest {
        withRecipient(validated = true)
        coEvery { sendEmail.invoke(any(), any(), any()) } returns Result.success(Unit)

        viewModel.sendEmail()

        // idAph comes from the route, not from the form: sending someone else's record
        // to a doctor would be a clinical error, not just a bug.
        coVerify { sendEmail.invoke(idAph = ID_APH, to = RECIPIENT, body = BODY) }
        Assert.assertNotEquals(null, viewModel.uiState.infoEvent)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `a failed send is reported and does not claim success`() = runTest {
        withRecipient(validated = true)
        coEvery { sendEmail.invoke(any(), any(), any()) } returns Result.failure(Throwable())

        viewModel.sendEmail()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorEvent?.title)
        Assert.assertEquals(null, viewModel.uiState.infoEvent)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `cancelling asks to go back`() = runTest {
        viewModel.cancel()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `navigating to main marks the send as done`() = runTest {
        viewModel.navigateToMain()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.send)
    }

    @Test
    fun `consuming the navigation event clears the confirmation too`() = runTest {
        withRecipient(validated = true)
        coEvery { sendEmail.invoke(any(), any(), any()) } returns Result.success(Unit)
        viewModel.sendEmail()

        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(null, viewModel.uiState.infoEvent)
        Assert.assertEquals(false, viewModel.uiState.validateFields)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `the re-auth banner logs the user out and announces it`() = runTest {
        coEvery { logoutCurrentUser.invoke() } returns Result.success(USERNAME)

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        coVerify { logoutCurrentUser.invoke() }
        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(USERNAME) }
    }

    @Test
    fun `a failed logout still announces it so the user is not stranded`() = runTest {
        coEvery { logoutCurrentUser.invoke() } returns Result.failure(Throwable("boom"))

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `any other action only dismisses the banner`() = runTest {
        viewModel.handleEvent(GenericUiAction.DismissAction)

        Assert.assertEquals(null, viewModel.uiState.errorEvent)
        coVerify(exactly = 0) { logoutCurrentUser.invoke() }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
