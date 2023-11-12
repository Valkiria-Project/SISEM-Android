package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.DeleteAccessToken
import com.skgtecnologia.sisem.domain.deviceauth.usecases.AssociateDevice
import com.skgtecnologia.sisem.domain.deviceauth.usecases.GetDeviceAuthScreen
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.LOGIN
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
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

private const val CONTINUE_TITLE = "Continuar"

class DeviceAuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var associateDevice: AssociateDevice

    @MockK
    private lateinit var getDeviceAuthScreen: GetDeviceAuthScreen

    @MockK
    private lateinit var deleteAccessToken: DeleteAccessToken

    private lateinit var viewModel: DeviceAuthViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when getDeviceAuthScreen is success`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(screenModel)

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )

        Assert.assertEquals(screenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getDeviceAuthScreen is failure`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.failure(Throwable())

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when isAssociateDevice and isValidVehicleCode is false`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )
        viewModel.isValidVehicleCode = false
        viewModel.associateDevice()

        Assert.assertEquals(true, viewModel.uiState.validateFields)
    }

    @Test
    fun `when associateDevice is failure`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(screenModel)
        coEvery { associateDevice.invoke(ANDROID_ID, any(), false) } returns Result.failure(
            Throwable()
        )

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )
        viewModel.isValidVehicleCode = true
        viewModel.associateDevice()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when associateDevice is success`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)
        coEvery { associateDevice.invoke(ANDROID_ID, any(), true) } returns Result.success(mockk())

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )
        viewModel.isValidVehicleCode = true
        viewModel.disassociateDeviceState = true
        viewModel.associateDevice()

        Assert.assertEquals(false, viewModel.uiState.validateFields)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals("", viewModel.vehicleCode)
        Assert.assertEquals(false, viewModel.isValidVehicleCode)
        Assert.assertEquals(false, viewModel.disassociateDeviceState)
        Assert.assertEquals(CONTINUE_TITLE, viewModel.uiState.disassociateInfoModel?.title)
    }

    @Test
    fun `when disassociateDeviceState is false`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(screenModel)
        coEvery { associateDevice.invoke(ANDROID_ID, any(), false) } returns Result.success(mockk())
        coEvery { deleteAccessToken.invoke() } returns Result.success(Unit)

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )
        viewModel.isValidVehicleCode = true
        viewModel.disassociateDeviceState = false
        viewModel.associateDevice()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isCrewList)
    }

    @Test
    fun `when cancel is called from login`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)
        coEvery { deleteAccessToken.invoke() } returns Result.success(Unit)

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )
        viewModel.from = LOGIN
        viewModel.cancel()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isCancel)
        Assert.assertEquals(LOGIN, viewModel.uiState.navigationModel?.from)
    }

    @Test
    fun `when cancel is called from other`() = runTest {
        val from = "other"
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )
        viewModel.from = from
        viewModel.cancel()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isCancel)
        Assert.assertEquals(from, viewModel.uiState.navigationModel?.from)
    }

    @Test
    fun `when cancelBanner is called`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)
        coEvery { deleteAccessToken.invoke() } returns Result.success(Unit)

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )

        viewModel.cancelBanner()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isCrewList)
    }

    @Test
    fun `when consumeErrorEvent is called clear errorModel`() = runTest {
        coEvery { getDeviceAuthScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)

        viewModel = DeviceAuthViewModel(
            androidIdProvider,
            associateDevice,
            getDeviceAuthScreen,
            deleteAccessToken
        )

        viewModel.consumeErrorEvent()

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }

    private val screenModel = ScreenModel(
        body = listOf(
            SegmentedSwitchUiModel(
                identifier = "title",
                text = "description",
                textStyle = TextStyle.BODY_1,
                options = emptyList(),
                arrangement = Arrangement.Center,
                modifier = Modifier
            )
        )
    )
}
