package com.skgtecnologia.sisem.data.biometric.remote

import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricExistsResponse
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.domain.biometric.model.BiometricRegistrationStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

private const val DOCUMENT_NUMBER = "202607251"

class BiometricRemoteDataSourceTest {

    @MockK
    private lateinit var biometricApi: BiometricApi

    @MockK
    private lateinit var networkApi: NetworkApi

    private lateinit var biometricRemoteDataSource: BiometricRemoteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        biometricRemoteDataSource = BiometricRemoteDataSource(biometricApi, networkApi)
    }

    @Test
    fun `when exists returns 200 with exists true it maps to Registered with the payload`() = runTest {
        coEvery { biometricApi.biometricExists(DOCUMENT_NUMBER) } returns Response.success(
            BiometricExistsResponse(
                userName = "Q",
                userLastName = "CONDUCTOR",
                documentNumber = DOCUMENT_NUMBER,
                role = "medico",
                exists = true
            )
        )

        val status = biometricRemoteDataSource.exists(DOCUMENT_NUMBER)

        Assert.assertEquals(
            BiometricRegistrationStatus.Registered(
                userName = "Q",
                userLastName = "CONDUCTOR",
                documentNumber = DOCUMENT_NUMBER,
                role = "medico"
            ),
            status
        )
    }

    @Test
    fun `when exists returns 200 with exists false it maps to NotRegistered`() = runTest {
        coEvery { biometricApi.biometricExists(DOCUMENT_NUMBER) } returns Response.success(
            BiometricExistsResponse(exists = false)
        )

        val status = biometricRemoteDataSource.exists(DOCUMENT_NUMBER)

        Assert.assertEquals(BiometricRegistrationStatus.NotRegistered(), status)
    }

    @Test
    fun `when exists returns 200 with exists false it still carries identity data if present`() = runTest {
        coEvery { biometricApi.biometricExists(DOCUMENT_NUMBER) } returns Response.success(
            BiometricExistsResponse(
                userName = "Q",
                userLastName = "CONDUCTOR",
                documentNumber = DOCUMENT_NUMBER,
                role = "medico",
                exists = false
            )
        )

        val status = biometricRemoteDataSource.exists(DOCUMENT_NUMBER)

        Assert.assertEquals(
            BiometricRegistrationStatus.NotRegistered(
                userName = "Q",
                userLastName = "CONDUCTOR",
                documentNumber = DOCUMENT_NUMBER,
                role = "medico"
            ),
            status
        )
    }

    @Test
    fun `when exists returns 404 it maps to NotRegistered instead of a network error`() = runTest {
        coEvery { biometricApi.biometricExists(DOCUMENT_NUMBER) } returns Response.error(
            404,
            "".toResponseBody()
        )

        val status = biometricRemoteDataSource.exists(DOCUMENT_NUMBER)

        Assert.assertEquals(BiometricRegistrationStatus.NotRegistered(), status)
    }

    @Test
    fun `when exists returns a 5xx it maps to QueryError`() = runTest {
        coEvery { biometricApi.biometricExists(DOCUMENT_NUMBER) } returns Response.error(
            500,
            "".toResponseBody()
        )

        val status = biometricRemoteDataSource.exists(DOCUMENT_NUMBER)

        Assert.assertEquals(BiometricRegistrationStatus.QueryError, status)
    }

    @Test
    fun `when exists throws an IOException it maps to NetworkIntermittency`() = runTest {
        coEvery { biometricApi.biometricExists(DOCUMENT_NUMBER) } throws IOException("no connection")

        val status = biometricRemoteDataSource.exists(DOCUMENT_NUMBER)

        Assert.assertEquals(BiometricRegistrationStatus.NetworkIntermittency, status)
    }
}
