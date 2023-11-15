package com.skgtecnologia.sisem.data.stretcherretention

import com.skgtecnologia.sisem.commons.CODE
import com.skgtecnologia.sisem.commons.INCIDENT_CODE
import com.skgtecnologia.sisem.commons.PATIENT_ID
import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.stretcherretention.remote.StretcherRetentionRemoteDataSource
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StretcherRetentionRepositoryImplTest {

    @MockK
    private lateinit var stretcherRetentionRemoteDataSource: StretcherRetentionRemoteDataSource

    @MockK
    private lateinit var operationCacheDataSource: OperationCacheDataSource

    private lateinit var stretcherRetentionRepositoryImpl: StretcherRetentionRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        stretcherRetentionRepositoryImpl = StretcherRetentionRepositoryImpl(
            stretcherRetentionRemoteDataSource = stretcherRetentionRemoteDataSource,
            operationCacheDataSource = operationCacheDataSource
        )
    }

    @Test
    fun `when getStretcherRetentionScreen is called`() = runTest {
        val operationModel = mockk<OperationModel> {
            coEvery { vehicleCode } returns CODE
        }
        coEvery {
            stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(emptyScreenModel)
        coEvery { operationCacheDataSource.observeOperationConfig() } returns flow {
            emit(operationModel)
        }

        val result = stretcherRetentionRepositoryImpl.getStretcherRetentionScreen(
            serial = SERIAL,
            incidentCode = INCIDENT_CODE,
            patientId = PATIENT_ID
        )

        Assert.assertEquals(emptyScreenModel, result)
    }

    @Test
    fun `when saveStretcherRetention is called`() = runTest {
        coEvery {
            stretcherRetentionRemoteDataSource.saveStretcherRetention(
                any(),
                any(),
                any()
            )
        } returns Result.success(Unit)

        stretcherRetentionRepositoryImpl.saveStretcherRetention(
            fieldsValue = emptyMap(),
            chipSelectionValues = emptyMap()
        )
    }
}
