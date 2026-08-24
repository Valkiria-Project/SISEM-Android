package com.skgtecnologia.sisem.ui.biometric

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.biometric.model.BiometricRegistrationStatus
import com.skgtecnologia.sisem.domain.biometric.usecases.GetBiometricRegistrationStatus
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

private const val DOCUMENT_NUMBER = "202607251"

@RunWith(RobolectricTestRunner::class)
class BiometricEnrollmentViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var getAllAccessTokens: GetAllAccessTokens

    @MockK
    lateinit var getBiometricRegistrationStatus: GetBiometricRegistrationStatus

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = MainRoute.BiometricEnrollmentRoute(document = DOCUMENT_NUMBER)
    )

    private lateinit var viewModel: BiometricEnrollmentViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun buildViewModel() {
        viewModel = BiometricEnrollmentViewModel(
            savedStateHandle,
            getAllAccessTokens = getAllAccessTokens,
            getBiometricRegistrationStatus = getBiometricRegistrationStatus
        )
    }

    @Test
    fun `when status is Registered it populates crewMember from the exists payload`() = runTest {
        coEvery { getAllAccessTokens.invoke() } returns Result.success(emptyList())
        coEvery { getBiometricRegistrationStatus(DOCUMENT_NUMBER) } returns
            BiometricRegistrationStatus.Registered(
                userName = "Q",
                userLastName = "CONDUCTOR",
                documentNumber = DOCUMENT_NUMBER,
                role = "medico"
            )

        buildViewModel()

        Assert.assertEquals(true, viewModel.uiState.value.isRegistered)
        Assert.assertEquals("Q CONDUCTOR", viewModel.uiState.value.crewMember?.name)
        Assert.assertEquals(DOCUMENT_NUMBER, viewModel.uiState.value.crewMember?.document)
        Assert.assertEquals("Médico", viewModel.uiState.value.crewMember?.role)
        Assert.assertEquals(null, viewModel.uiState.value.errorModel)
    }

    @Test
    fun `when status is NotRegistered it shows the blue not-registered banner`() = runTest {
        coEvery { getAllAccessTokens.invoke() } returns Result.success(emptyList())
        coEvery { getBiometricRegistrationStatus(DOCUMENT_NUMBER) } returns
            BiometricRegistrationStatus.NotRegistered

        buildViewModel()

        Assert.assertEquals(false, viewModel.uiState.value.isRegistered)
        Assert.assertEquals("#42A4FA", viewModel.uiState.value.errorModel?.iconColor)
    }

    @Test
    fun `when status is NetworkIntermittency it keeps local crew data and shows a network banner`() = runTest {
        val token = accessTokenModel(document = DOCUMENT_NUMBER)
        coEvery { getAllAccessTokens.invoke() } returns Result.success(listOf(token))
        coEvery { getBiometricRegistrationStatus(DOCUMENT_NUMBER) } returns
            BiometricRegistrationStatus.NetworkIntermittency

        buildViewModel()

        Assert.assertEquals(token.nameUser, viewModel.uiState.value.crewMember?.name)
        Assert.assertNotNull(viewModel.uiState.value.errorModel)
    }

    @Test
    fun `when status is QueryError it surfaces the query error banner`() = runTest {
        coEvery { getAllAccessTokens.invoke() } returns Result.success(emptyList())
        coEvery { getBiometricRegistrationStatus(DOCUMENT_NUMBER) } returns
            BiometricRegistrationStatus.QueryError

        buildViewModel()

        Assert.assertNotNull(viewModel.uiState.value.errorModel)
        Assert.assertEquals(false, viewModel.uiState.value.isRegistered)
    }

    @Test
    fun `when getAllAccessTokens fails it surfaces the error and stops loading`() = runTest {
        coEvery { getAllAccessTokens.invoke() } returns Result.failure(Throwable("boom"))

        buildViewModel()

        Assert.assertEquals(false, viewModel.uiState.value.isLoading)
        Assert.assertNotNull(viewModel.uiState.value.errorModel)
    }

    private fun accessTokenModel(document: String) = AccessTokenModel(
        userId = 1,
        dateTime = LocalDateTime.now(),
        accessToken = "access",
        refreshToken = "refresh",
        tokenType = "Bearer",
        username = "q.conductor",
        role = "DRIVER",
        isAdmin = false,
        nameUser = "Q Conductor",
        preoperational = null,
        turn = null,
        isWarning = false,
        docType = "CC",
        document = document,
        refreshDateTime = LocalDateTime.now(),
        expDate = LocalDateTime.now().plusDays(1)
    )
}
