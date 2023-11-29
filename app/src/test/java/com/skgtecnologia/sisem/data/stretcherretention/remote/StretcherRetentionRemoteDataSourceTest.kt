package com.skgtecnologia.sisem.data.stretcherretention.remote

import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.commons.buttonResponseMock
import com.skgtecnologia.sisem.commons.footerResponseMock
import com.skgtecnologia.sisem.commons.headerResponseMock
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class StretcherRetentionRemoteDataSourceTest {

    @MockK
    private lateinit var stretcherRetentionApi: StretcherRetentionApi

    private lateinit var stretcherRetentionRemoteDataSource: StretcherRetentionRemoteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        stretcherRetentionRemoteDataSource =
            StretcherRetentionRemoteDataSource(stretcherRetentionApi)
    }

    @Test
    fun `when getStretcherRetentionScreen is success`() = runTest {
        val screenResponse = ScreenResponse(
            header = headerResponseMock,
            body = listOf(buttonResponseMock),
            footer = footerResponseMock
        )
        coEvery {
            stretcherRetentionApi.getStretcherRetentionScreen(any())
        } returns Response.success(screenResponse)

        val result = stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
            idAph = ID_APH.toString()
        )

        Assert.assertEquals(true, result.isSuccess)
        Assert.assertEquals(screenResponse.mapToDomain(), result.getOrThrow())
    }

    @Test
    fun `when getStretcherRetentionScreen is failure`() = runTest {
        coEvery {
            stretcherRetentionApi.getStretcherRetentionScreen(any())
        } returns Response.error(400, "".toResponseBody())

        val result = stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
            idAph = ID_APH.toString()
        )

        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `when saveStretcherRetention is success`() = runTest {
        coEvery {
            stretcherRetentionApi.saveStretcherRetention(any())
        } returns Response.success(Unit)

        val result = stretcherRetentionRemoteDataSource.saveStretcherRetention(
            idAph = ID_APH.toString(),
            fieldsValue = mapOf(),
            chipSelectionValues = mapOf()
        )

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when saveStretcherRetention is failure`() = runTest {
        coEvery {
            stretcherRetentionApi.saveStretcherRetention(any())
        } returns Response.error(400, "".toResponseBody())

        val result = stretcherRetentionRemoteDataSource.saveStretcherRetention(
            idAph = ID_APH.toString(),
            fieldsValue = mapOf(),
            chipSelectionValues = mapOf()
        )

        Assert.assertEquals(true, result.isFailure)
    }
}
