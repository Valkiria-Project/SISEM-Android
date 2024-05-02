package com.skgtecnologia.sisem.data.preoperational

import com.skgtecnologia.sisem.commons.DRIVER_ROLE
import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.preoperational.remote.PreOperationalRemoteDataSource
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PreOperationalRepositoryImplTest {

    @MockK
    private lateinit var authCacheDataSource: AuthCacheDataSource

    @MockK
    private lateinit var operationCacheDataSource: OperationCacheDataSource

    @MockK
    private lateinit var preOperationalRemoteDataSource: PreOperationalRemoteDataSource

    private lateinit var preOperationalRepositoryImpl: PreOperationalRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        preOperationalRepositoryImpl = PreOperationalRepositoryImpl(
            authCacheDataSource,
            operationCacheDataSource,
            preOperationalRemoteDataSource
        )
    }

    @Test
    fun `when getPreOperationalScreen is called`() = runTest {
        val accessTokenModel = mockk<AccessTokenModel> {
            coEvery { role } returns OperationRole.LEAD_APH.name
            coEvery { turn } returns mockk {
                coEvery { id } returns 1
            }
        }
        coEvery { authCacheDataSource.observeAccessToken() } returns flow { emit(accessTokenModel) }
        coEvery {
            authCacheDataSource.retrieveAccessTokenByRole(DRIVER_ROLE.lowercase())
        } returns accessTokenModel
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flow { emit(null) }
        coEvery {
            preOperationalRemoteDataSource.getPreOperationalScreen(
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(emptyScreenModel)

        val result = preOperationalRepositoryImpl.getPreOperationalScreen(DRIVER_ROLE, SERIAL)

        Assert.assertEquals(emptyScreenModel, result)
    }

    @Test
    fun `when getAuthCardViewScreen is called`() = runTest {
        val accessTokenModel = mockk<AccessTokenModel> {
            coEvery { turn } returns mockk {
                coEvery { id } returns 1
            }
        }
        coEvery { authCacheDataSource.observeAccessToken() } returns flow { emit(accessTokenModel) }
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flow { emit(null) }
        coEvery {
            preOperationalRemoteDataSource.getAuthCardViewScreen(
                any(),
                any(),
                any()
            )
        } returns Result.success(emptyScreenModel)

        val result = preOperationalRepositoryImpl.getAuthCardViewScreen(SERIAL)

        Assert.assertEquals(emptyScreenModel, result)
    }

    @Test
    fun `when getPreOperationalViewScreen is called`() = runTest {
        val accessTokenModel = mockk<AccessTokenModel> {
            coEvery { turn } returns mockk {
                coEvery { id } returns 1
            }
        }
        coEvery { authCacheDataSource.observeAccessToken() } returns flow { emit(accessTokenModel) }
        coEvery { authCacheDataSource.retrieveAccessTokenByRole(any()) } returns accessTokenModel
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flow { emit(null) }
        coEvery {
            preOperationalRemoteDataSource.getPreOperationalScreenView(
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(emptyScreenModel)

        val result =
            preOperationalRepositoryImpl.getPreOperationalViewScreen(SERIAL, OperationRole.LEAD_APH)

        Assert.assertEquals(emptyScreenModel, result)
    }

    @Test
    fun `when getRole is called`() = runTest {
        val accessTokenModel = mockk<AccessTokenModel> {
            coEvery { role } returns OperationRole.DRIVER.name
        }
        coEvery { authCacheDataSource.observeAccessToken() } returns flow { emit(accessTokenModel) }

        val result = preOperationalRepositoryImpl.getRole()

        Assert.assertEquals(OperationRole.DRIVER, result)
    }

    @Test
    fun `when sendPreOperational is called`() = runTest {
        val accessTokenModel = mockk<AccessTokenModel> {
            coEvery { role } returns OperationRole.LEAD_APH.name
            coEvery { turn } returns mockk {
                coEvery { id } returns 1
            }
        }
        coEvery { authCacheDataSource.observeAccessToken() } returns flow { emit(accessTokenModel) }
        coEvery {
            preOperationalRemoteDataSource.sendPreOperational(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(Unit)
        coEvery { authCacheDataSource.updatePreOperationalStatus(any(), false) } returns Unit
        coEvery {
            preOperationalRemoteDataSource.sendFindings(
                any(),
                any(),
                any()
            )
        } returns Result.success(Unit)

        preOperationalRepositoryImpl.sendPreOperational(
            ,
            findings = emptyMap(),
            inventoryValues = emptyMap(),
            fieldsValues = emptyMap(),
            novelties = emptyList()
        )
    }
}
