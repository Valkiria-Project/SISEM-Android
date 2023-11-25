package com.skgtecnologia.sisem.data.stretcherretention

import com.skgtecnologia.sisem.commons.CODE
import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.data.stretcherretention.remote.StretcherRetentionRemoteDataSource
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StretcherRetentionRepositoryImplTest {

    @MockK
    private lateinit var stretcherRetentionRemoteDataSource: StretcherRetentionRemoteDataSource

    private lateinit var stretcherRetentionRepositoryImpl: StretcherRetentionRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        stretcherRetentionRepositoryImpl = StretcherRetentionRepositoryImpl(
            stretcherRetentionRemoteDataSource = stretcherRetentionRemoteDataSource
        )
    }

    @Test
    fun `when getStretcherRetentionScreen is called`() = runTest {
        val operationModel = mockk<OperationModel> {
            coEvery { vehicleCode } returns CODE
        }
        coEvery {
            stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
                idAph = ID_APH
            )
        } returns Result.success(emptyScreenModel)

        val result = stretcherRetentionRepositoryImpl.getStretcherRetentionScreen(idAph = ID_APH)

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
