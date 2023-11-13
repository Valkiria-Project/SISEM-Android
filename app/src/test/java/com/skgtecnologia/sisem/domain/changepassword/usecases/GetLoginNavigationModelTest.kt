package com.skgtecnologia.sisem.domain.changepassword.usecases

import com.skgtecnologia.sisem.commons.CODE
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.model.PreOperationalModel
import com.skgtecnologia.sisem.domain.auth.model.TurnModel
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
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

class GetLoginNavigationModelTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var operationRepository: OperationRepository

    private lateinit var getLoginNavigationModel: GetLoginNavigationModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getLoginNavigationModel = GetLoginNavigationModel(authRepository, operationRepository)
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
        }
        val operationConfig = mockk<OperationModel> {
            every { vehicleCode } returns CODE
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { emit(accessToken) }
        coEvery { operationRepository.observeOperationConfig() } returns flow {
            emit(operationConfig)
        }

        val result = getLoginNavigationModel.invoke()
        val loginNavigationModel = result.getOrThrow()

        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(false, loginNavigationModel.isAdmin)
        Assert.assertEquals(true, loginNavigationModel.isTurnComplete)
        Assert.assertEquals(true, loginNavigationModel.requiresPreOperational)
        Assert.assertEquals(OperationRole.MEDIC_APH, loginNavigationModel.preOperationRole)
        Assert.assertEquals(false, loginNavigationModel.requiresDeviceAuth)
    }

    @Test
    fun `when observeCurrentAccessToken is success, observeOperationConfig is error`() = runTest {
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
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { emit(accessToken) }
        coEvery { operationRepository.observeOperationConfig() } throws Exception()

        val result = getLoginNavigationModel.invoke()

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when observeCurrentAccessToken is error, observeOperationConfig is success`() = runTest {
        val operationConfig = mockk<OperationModel> {
            every { vehicleCode } returns CODE
        }
        coEvery { authRepository.observeCurrentAccessToken() } throws Exception()
        coEvery { operationRepository.observeOperationConfig() } returns flow {
            emit(operationConfig)
        }

        val result = getLoginNavigationModel.invoke()

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when observeCurrentAccessToken and observeOperationConfig are error`() = runTest {
        coEvery { authRepository.observeCurrentAccessToken() } throws Exception()
        coEvery { operationRepository.observeOperationConfig() } throws Exception()

        val result = getLoginNavigationModel.invoke()

        Assert.assertEquals(true, result.isFailure)
    }
}
