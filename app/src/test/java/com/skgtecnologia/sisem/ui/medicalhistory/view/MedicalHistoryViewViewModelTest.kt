package com.skgtecnologia.sisem.ui.medicalhistory.view

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.DeleteAphFile
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryViewScreen
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SaveAphFiles
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.operation.usecases.GetOperationConfigWithCurrentRole
import com.skgtecnologia.sisem.ui.navigation.AphRoute
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import com.valkiria.uicomponents.components.media.MediaItemUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
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
import java.io.File

private const val ID_APH = "APH-7788"

@RunWith(RobolectricTestRunner::class)
class MedicalHistoryViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var deleteAphFile: DeleteAphFile

    @MockK
    private lateinit var getMedicalHistoryViewScreen: GetMedicalHistoryViewScreen

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getOperationConfigWithCurrentRole: GetOperationConfigWithCurrentRole

    @MockK
    private lateinit var saveAphFiles: SaveAphFiles

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = AphRoute.MedicalHistoryViewRoute(idAph = ID_APH)
    )

    private lateinit var viewModel: MedicalHistoryViewViewModel

    /**
     * The screen must carry a media section: the view model reaches for it with `first()`
     * while loading, so a body without one blows up before any of this can be asserted.
     */
    private val screenWithMedia = ScreenModel(
        body = listOf(MediaActionsUiModel(selectedMediaUris = listOf("existing.jpg")))
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        every { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) } just runs

        coEvery { getMedicalHistoryViewScreen.invoke(any()) } returns Result.success(
            screenWithMedia
        )
        coEvery { getOperationConfigWithCurrentRole.invoke() } returns Result.success(mockk())
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    private fun createViewModel() = MedicalHistoryViewViewModel(
        savedStateHandle,
        deleteAphFile,
        getMedicalHistoryViewScreen,
        logoutCurrentUser,
        getOperationConfigWithCurrentRole,
        saveAphFiles
    )

    private fun media(name: String, sizeValid: Boolean) = MediaItemUiModel(
        uri = "content://$name",
        name = name,
        isSizeValid = sizeValid
    )

    @Test
    fun `the existing attachments are loaded with the screen`() = runTest {
        viewModel = createViewModel()

        Assert.assertEquals(1, viewModel.uiState.selectedMediaItems.size)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when the screen fails to load the error is surfaced`() = runTest {
        coEvery { getMedicalHistoryViewScreen.invoke(any()) } returns Result.failure(Throwable())

        viewModel = createViewModel()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorEvent?.title)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `an oversized photo is refused and explained`() = runTest {
        viewModel = createViewModel()
        val before = viewModel.uiState.selectedMediaItems.size

        viewModel.onPhotoTaken(media("huge.jpg", sizeValid = false))

        // The attachment is what gets sent to the clinical record, so an oversized one
        // must not be kept quietly - it is dropped and the user is told why.
        Assert.assertEquals(before, viewModel.uiState.selectedMediaItems.size)
        Assert.assertNotEquals(null, viewModel.uiState.errorEvent)
    }

    @Test
    fun `a photo within the limit is kept without complaint`() = runTest {
        viewModel = createViewModel()
        val before = viewModel.uiState.selectedMediaItems.size

        viewModel.onPhotoTaken(media("ok.jpg", sizeValid = true))

        Assert.assertEquals(before + 1, viewModel.uiState.selectedMediaItems.size)
        Assert.assertEquals(null, viewModel.uiState.errorEvent)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.photoTaken)
    }

    @Test
    fun `picking several attachments keeps only the ones within the limit`() = runTest {
        viewModel = createViewModel()
        val before = viewModel.uiState.selectedMediaItems.size

        viewModel.updateMediaActions(
            listOf(media("ok.jpg", sizeValid = true), media("huge.jpg", sizeValid = false))
        )

        Assert.assertEquals(before + 1, viewModel.uiState.selectedMediaItems.size)
        Assert.assertNotEquals(null, viewModel.uiState.errorEvent)
    }

    @Test
    fun `refreshing without new attachments leaves the selection untouched`() = runTest {
        viewModel = createViewModel()
        viewModel.onPhotoTaken(media("ok.jpg", sizeValid = true))
        val selection = viewModel.uiState.selectedMediaItems

        viewModel.updateMediaActions(null)

        Assert.assertEquals(selection, viewModel.uiState.selectedMediaItems)
    }

    @Test
    fun `removing an attachment drops it from the selection`() = runTest {
        coEvery { deleteAphFile.invoke(any(), any()) } returns Result.success(Unit)
        viewModel = createViewModel()
        viewModel.onPhotoTaken(media("ok.jpg", sizeValid = true))
        val before = viewModel.uiState.selectedMediaItems.size

        viewModel.removeMediaActionsImage(0)

        Assert.assertEquals(before - 1, viewModel.uiState.selectedMediaItems.size)
    }

    @Test
    fun `sending without attachments skips the upload entirely`() = runTest {
        viewModel = createViewModel()

        viewModel.sendMedicalHistoryView(images = emptyList(), description = null)

        Assert.assertEquals(ID_APH, viewModel.uiState.navigationModel?.sendMedical)
        coVerify(exactly = 0) { saveAphFiles.invoke(any(), any(), any()) }
    }

    @Test
    fun `sending attachments uploads them against the record from the route`() = runTest {
        coEvery { saveAphFiles.invoke(any(), any(), any()) } returns Result.success(Unit)
        viewModel = createViewModel()

        viewModel.sendMedicalHistoryView(listOf(File("one.jpg")), description = "nota")

        coVerify {
            saveAphFiles.invoke(
                idAph = ID_APH,
                images = match { it.single().fileName == "Img_${ID_APH}_0.jpg" },
                description = "nota"
            )
        }
        Assert.assertEquals(ID_APH, viewModel.uiState.navigationModel?.sendMedical)
    }

    @Test
    fun `a failed upload does not pretend the record was sent`() = runTest {
        coEvery { saveAphFiles.invoke(any(), any(), any()) } returns Result.failure(Throwable())
        viewModel = createViewModel()

        viewModel.sendMedicalHistoryView(listOf(File("one.jpg")), description = null)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorEvent?.title)
        Assert.assertEquals(null, viewModel.uiState.navigationModel?.sendMedical)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `the re-auth banner logs the user out and announces it`() = runTest {
        coEvery { logoutCurrentUser.invoke() } returns Result.success(USERNAME)
        viewModel = createViewModel()

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        coVerify { logoutCurrentUser.invoke() }
        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(USERNAME) }
    }

    @Test
    fun `any other action only dismisses the banner`() = runTest {
        viewModel = createViewModel()

        viewModel.handleEvent(GenericUiAction.DismissAction)

        Assert.assertEquals(null, viewModel.uiState.errorEvent)
        coVerify(exactly = 0) { logoutCurrentUser.invoke() }
    }

    @Test
    fun `going back and consuming the event clears the destination`() = runTest {
        viewModel = createViewModel()

        viewModel.goBack()
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)

        viewModel.consumeNavigationEvent()
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }
}
