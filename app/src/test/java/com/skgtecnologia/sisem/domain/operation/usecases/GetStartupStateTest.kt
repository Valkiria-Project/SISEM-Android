package com.skgtecnologia.sisem.domain.operation.usecases

import com.skgtecnologia.sisem.commons.CODE
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.model.PreOperationalModel
import com.skgtecnologia.sisem.domain.auth.model.TurnModel
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel
import com.skgtecnologia.sisem.domain.operation.model.PreoperationalStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetStartupStateTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var observeOperationConfig: ObserveOperationConfig

    private lateinit var getStartupState: GetStartupState

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getStartupState = GetStartupState(authRepository, observeOperationConfig)
    }

    @Test
    fun `when observeCurrentAccessToken and observeOperationConfig are success`() = runTest {
        val turnMock = mockk<TurnModel> {
            coEvery { isComplete } returns true
        }
        val preOperationalMock = mockk<PreOperationalModel> {
            coEvery { status } returns true
        }
        val accessToken = mockk<AccessTokenModel> {
            coEvery { isAdmin } returns false
            coEvery { turn } returns turnMock
            coEvery { preoperational } returns preOperationalMock
            coEvery { role } returns OperationRole.MEDIC_APH.name
            coEvery { isWarning } returns false
        }
        val operationConfig = mockk<OperationModel> {
            every { vehicleCode } returns CODE
            every { vehicleConfig?.preoperational } returns PreoperationalStatus.SI.value
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { emit(accessToken) }
        coEvery { accessToken.copy(configPreoperational = any()) } returns accessToken
        coEvery { observeOperationConfig() } returns Result.success(operationConfig)

        val result = getStartupState()
        val startupNavigationModel = result.getOrThrow()

        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(false, startupNavigationModel.isAdmin)
        Assert.assertEquals(true, startupNavigationModel.isTurnStarted)
        Assert.assertEquals(false, startupNavigationModel.isWarning)
        Assert.assertEquals(true, startupNavigationModel.requiresPreOperational)
        Assert.assertEquals(OperationRole.MEDIC_APH, startupNavigationModel.preOperationRole)
        Assert.assertEquals(CODE, startupNavigationModel.vehicleCode)
    }

    @Test
    fun `when observeCurrentAccessToken is success and observeOperationConfig failure`() = runTest {
        val turnMock = mockk<TurnModel> {
            coEvery { isComplete } returns true
        }
        val preOperationalMock = mockk<PreOperationalModel> {
            coEvery { status } returns true
        }
        val accessToken = mockk<AccessTokenModel> {
            coEvery { isAdmin } returns false
            coEvery { turn } returns turnMock
            coEvery { preoperational } returns preOperationalMock
            coEvery { role } returns OperationRole.MEDIC_APH.name
            coEvery { isWarning } returns true
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { emit(accessToken) }
        coEvery { observeOperationConfig() } returns Result.failure(Exception())

        val result = getStartupState()
        val startupNavigationModel = result.getOrThrow()

        Assert.assertEquals(false, result.isFailure)
        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(false, startupNavigationModel.isAdmin)
        Assert.assertEquals(true, startupNavigationModel.isTurnStarted)
        Assert.assertEquals(true, startupNavigationModel.isWarning)
        Assert.assertEquals(true, startupNavigationModel.requiresPreOperational)
        Assert.assertEquals(OperationRole.MEDIC_APH, startupNavigationModel.preOperationRole)
        Assert.assertEquals(null, startupNavigationModel.vehicleCode)
    }

    @Test
    fun `when observeCurrentAccessToken is failure and observeOperationConfig is success`() =
        runTest {
            val operationConfig = mockk<OperationModel> {
                every { vehicleCode } returns CODE
            }
            coEvery { authRepository.observeCurrentAccessToken() } returns flow {
                throw IllegalStateException()
            }
            coEvery { observeOperationConfig() } returns Result.success(operationConfig)

            val result = getStartupState()

            Assert.assertEquals(true, result.isFailure)
        }

    @Test
    fun `when observeCurrentAccessToken and observeOperationConfig are failure`() = runTest {
        coEvery { authRepository.observeCurrentAccessToken() } returns flow {
            throw IllegalStateException()
        }
        coEvery { observeOperationConfig() } returns Result.failure(IllegalStateException())

        val result = getStartupState()

        Assert.assertEquals(true, result.isFailure)
    }
}
