package com.skgtecnologia.sisem.data.remote.interceptors

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.skgtecnologia.sisem.ui.commons.extensions.locationFlow
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

private const val LOCATION_EXTENSIONS =
    "com.skgtecnologia.sisem.ui.commons.extensions.LocationExtensionsKt"
private const val GEOLOCATION_HEADER = "geolocation"
private const val UNAVAILABLE = "UNAVAILABLE_LOCATION"

class AuditInterceptorTest {

    @MockK
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @MockK
    private lateinit var chain: Interceptor.Chain

    private lateinit var interceptor: AuditInterceptor
    private val sentRequests = mutableListOf<Request>()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(LOCATION_EXTENSIONS)

        // The IP lookup builds its own OkHttpClient. Make it fail so the test stays off
        // the network; the interceptor already degrades to a placeholder header.
        mockkConstructor(OkHttpClient::class)
        every { constructedWith<OkHttpClient>().newCall(any()) } throws IOException("offline")

        interceptor = AuditInterceptor(fusedLocationClient)

        val request = Request.Builder().url("https://example.com/api/data").build()
        every { chain.request() } returns request
        every { chain.proceed(capture(sentRequests)) } answers {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }
    }

    @After
    fun teardown() {
        unmockkStatic(LOCATION_EXTENSIONS)
        unmockkConstructor(OkHttpClient::class)
        sentRequests.clear()
    }

    private fun givenLocation(latitude: Double, longitude: Double) {
        val location = mockk<Location>()
        every { location.latitude } returns latitude
        every { location.longitude } returns longitude
        every { fusedLocationClient.locationFlow() } returns flowOf(location)
    }

    @Test
    fun `the location is read once and reused while the entry is fresh`() {
        givenLocation(latitude = 4.60971, longitude = -74.08175)

        repeat(3) { interceptor.intercept(chain) }

        verify(exactly = 1) { fusedLocationClient.locationFlow() }
        Assert.assertEquals(3, sentRequests.size)
        sentRequests.forEach {
            Assert.assertEquals("4.60971, -74.08175", it.header(GEOLOCATION_HEADER))
        }
    }

    @Test
    fun `a failed read reports the position as unavailable instead of guessing`() {
        every { fusedLocationClient.locationFlow() } returns flowOf()

        interceptor.intercept(chain)

        Assert.assertEquals(UNAVAILABLE, sentRequests.single().header(GEOLOCATION_HEADER))
    }

    // Expiry itself is not covered: the interceptor reads System.currentTimeMillis()
    // directly, so a test would have to sleep out the TTL. Making it verifiable means
    // injecting a clock, which is more than this change is worth on its own.
}
