package com.skgtecnologia.sisem.domain.operation.usecases

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
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

class GetOperationConfigWithCurrentRoleTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var operationRepository: OperationRepository

    private lateinit var getOperationConfigWithCurrentRole: GetOperationConfigWithCurrentRole

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getOperationConfigWithCurrentRole = GetOperationConfigWithCurrentRole(authRepository, operationRepository)
    }

    @Test
    fun `when observeOperationConfig and observeCurrentAccessToken are success`() = runTest {
        val accessTokenModel = mockk<AccessTokenModel> {
            coEvery { role } returns OperationRole.MEDIC_APH.name
        }
        val operationalModel = mockk<OperationModel>()
        every { operationalModel.copy(operationRole = any()) } returns operationalModel
        coEvery { operationRepository.observeOperationConfig() } returns flow {
            emit(operationalModel)
        }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow {
            emit(accessTokenModel)
        }

        val result = getOperationConfigWithCurrentRole()

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when observeOperationConfig is success and observeCurrentAccessToken failure`() = runTest {
        coEvery { operationRepository.observeOperationConfig() } returns mockk()
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { Throwable() }

        val result = getOperationConfigWithCurrentRole()

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when observeOperationConfig failure and observeCurrentAccessToken is success`() = runTest {
        val accessTokenModel = mockk<AccessTokenModel> {
            coEvery { role } returns OperationRole.MEDIC_APH.name
        }
        coEvery { operationRepository.observeOperationConfig() } returns flow { Throwable() }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow {
            emit(accessTokenModel)
        }

        val result = getOperationConfigWithCurrentRole()

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when observeOperationConfig and observeCurrentAccessToken failure`() = runTest {
        coEvery { operationRepository.observeOperationConfig() } returns flow { Throwable() }
        coEvery { authRepository.observeCurrentAccessToken() } returns flow { Throwable() }

        val result = getOperationConfigWithCurrentRole()

        Assert.assertEquals(true, result.isFailure)
    }
}
