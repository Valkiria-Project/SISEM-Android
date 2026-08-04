package com.skgtecnologia.sisem.ui.signature.init

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.signature.usecases.GetInitSignatureScreen
import com.skgtecnologia.sisem.domain.signature.usecases.SearchDocument
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

private const val DOCUMENT = "1032456789"

class InitSignatureViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getInitSignatureScreen: GetInitSignatureScreen

    @MockK
    private lateinit var searchDocument: SearchDocument

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    private lateinit var viewModel: InitSignatureViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        every { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) } just runs
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    private suspend fun givenScreenLoads(): InitSignatureViewModel {
        coEvery { getInitSignatureScreen.invoke() } returns Result.success(emptyScreenModel)
        return InitSignatureViewModel(getInitSignatureScreen, searchDocument, logoutCurrentUser)
    }

    private fun InitSignatureViewModel.withTypedDocument(validated: Boolean) {
        document.value = InputUiModel(
            identifier = "DOCUMENT",
            updatedValue = DOCUMENT,
            fieldValidated = validated
        )
    }

    @Test
    fun `when the screen loads it is exposed and the loader is cleared`() = runTest {
        viewModel = givenScreenLoads()

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when the screen fails to load the error is surfaced`() = runTest {
        coEvery { getInitSignatureScreen.invoke() } returns Result.failure(Throwable())

        viewModel = InitSignatureViewModel(
            getInitSignatureScreen,
            searchDocument,
            logoutCurrentUser
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `an unvalidated document is never sent to the backend`() = runTest {
        viewModel = givenScreenLoads()
        viewModel.withTypedDocument(validated = false)

        viewModel.searchDocument()

        // The field is flagged so the form can show why nothing happened, but the search
        // itself must not go out - and the screen must not move on.
        Assert.assertEquals(true, viewModel.uiState.validateFields)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
        coVerify(exactly = 0) { searchDocument.invoke(any()) }
    }

    @Test
    fun `a found document carries the identifier into the signature screen`() = runTest {
        viewModel = givenScreenLoads()
        viewModel.withTypedDocument(validated = true)
        coEvery { searchDocument.invoke(DOCUMENT) } returns Result.success(Unit)

        viewModel.searchDocument()

        Assert.assertEquals(DOCUMENT, viewModel.uiState.navigationModel?.document)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }

    @Test
    fun `a document that cannot be found leaves the user on the screen`() = runTest {
        viewModel = givenScreenLoads()
        viewModel.withTypedDocument(validated = true)
        coEvery { searchDocument.invoke(DOCUMENT) } returns Result.failure(Throwable())

        viewModel.searchDocument()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when goBack is called the navigation asks to go back`() = runTest {
        viewModel = givenScreenLoads()

        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when the navigation event is consumed the destination is cleared`() = runTest {
        viewModel = givenScreenLoads()
        viewModel.goBack()

        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `the re-auth banner logs the user out and announces it`() = runTest {
        viewModel = givenScreenLoads()
        coEvery { logoutCurrentUser.invoke() } returns Result.success(USERNAME)

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        coVerify { logoutCurrentUser.invoke() }
        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(USERNAME) }
    }

    @Test
    fun `a failed logout still announces it so the user is not stranded`() = runTest {
        viewModel = givenScreenLoads()
        coEvery { logoutCurrentUser.invoke() } returns Result.failure(Throwable("boom"))

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `any other action only dismisses the banner`() = runTest {
        coEvery { getInitSignatureScreen.invoke() } returns Result.failure(Throwable())
        viewModel = InitSignatureViewModel(
            getInitSignatureScreen,
            searchDocument,
            logoutCurrentUser
        )

        viewModel.handleEvent(GenericUiAction.DismissAction)

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
        coVerify(exactly = 0) { logoutCurrentUser.invoke() }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
