package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.commons.PASSWORD
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.usecases.GetOperationConfigWithCurrentRole
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoginTest {

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var getOperationConfigWithCurrentRole: GetOperationConfigWithCurrentRole

    private lateinit var login: Login

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        login = Login(authRepository, getOperationConfigWithCurrentRole)
    }

    @Test
    fun `when authenticate is success`() = runTest {
        val accessToken = mockk<AccessTokenModel>()
        coEvery { authRepository.authenticate(any(), any()) } returns accessToken
        coEvery { accessToken.copy(configPreoperational = any()) } returns accessToken
        coEvery { getOperationConfigWithCurrentRole.invoke() } returns mockk<Result<OperationModel>>()

        val result = login(USERNAME, PASSWORD)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when authenticate is failure`() = runTest {
        coEvery { authRepository.authenticate(any(), any()) } throws Exception()

        val result = login(USERNAME, PASSWORD)

        Assert.assertEquals(false, result.isSuccess)
    }
}
