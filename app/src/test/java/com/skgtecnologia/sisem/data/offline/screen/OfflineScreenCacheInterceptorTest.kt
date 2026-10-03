package com.skgtecnologia.sisem.data.offline.screen

import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.commons.security.BlobCipher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.IOException

private const val BASE = "https://api.example.test/sisem-api/v1/"
private val JSON = "application/json".toMediaType()

/**
 * Runs the interceptor inside a real OkHttp client. The last interceptor plays the network: it
 * answers, fails, or counts how often it was reached.
 */
class OfflineScreenCacheInterceptorTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val connectivity = MutableStateFlow(Connectivity.ONLINE)
    private val networkMonitor = object : NetworkMonitor {
        override val connectivity: StateFlow<Connectivity> = this@OfflineScreenCacheInterceptorTest.connectivity
    }

    private var networkCalls = 0
    private var network: (Request) -> Response = { error("network not set up") }

    private lateinit var client: OkHttpClient

    @Before
    fun setup() {
        client = clientWith(ScreenCacheStore(folder.newFolder("screens"), ReversingCipher()))
    }

    private fun clientWith(store: ScreenCacheStore) = OkHttpClient.Builder()
        .addInterceptor(OfflineScreenCacheInterceptor(store, networkMonitor))
        .addInterceptor(
            Interceptor { chain ->
            networkCalls++
            network(chain.request())
        }
        )
        .build()

    private fun screenRequest(path: String = "screen/aph", body: String = """{"params":{"id_aph":"1"}}""") =
        Request.Builder().url(BASE + path).post(body.toRequestBody(JSON)).build()

    private fun answer(body: String, code: Int = 200): (Request) -> Response = { request ->
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("x")
            .body(body.toResponseBody(JSON))
            .build()
    }

    private val noSignal: (Request) -> Response = { throw IOException("no route to host") }

    private fun call(request: Request) = client.newCall(request).execute()

    @Test
    fun `a screen that loads is passed through untouched`() {
        network = answer("""{"body":["aph"]}""")

        val response = call(screenRequest())

        assertEquals("""{"body":["aph"]}""", response.body!!.string())
        assertNull(response.header(OFFLINE_SOURCE_HEADER))
    }

    @Test
    fun `when the network fails the last good copy is served`() {
        network = answer("""{"body":["aph"]}""")
        call(screenRequest()).close()

        network = noSignal
        val response = call(screenRequest())

        assertEquals("""{"body":["aph"]}""", response.body!!.string())
        assertEquals("cache", response.header(OFFLINE_SOURCE_HEADER))
    }

    @Test(expected = IOException::class)
    fun `with no copy the network failure reaches the caller`() {
        network = noSignal

        call(screenRequest())
    }

    @Test
    fun `with no network at all the copy is served without waiting on the network`() {
        network = answer("""{"body":["aph"]}""")
        call(screenRequest()).close()
        networkCalls = 0

        connectivity.value = Connectivity.OFFLINE
        val response = call(screenRequest())

        assertEquals("""{"body":["aph"]}""", response.body!!.string())
        assertEquals(0, networkCalls)
    }

    @Test
    fun `a network Android could not validate is still tried`() {
        network = answer("""{"body":["old"]}""")
        call(screenRequest()).close()

        // A private APN that blocks Google's check: the copy must not shadow a live answer.
        connectivity.value = Connectivity.UNVALIDATED
        network = answer("""{"body":["new"]}""")
        val response = call(screenRequest())

        assertEquals("""{"body":["new"]}""", response.body!!.string())
    }

    @Test
    fun `one patient's screen is never served for another`() {
        network = answer("""{"patient":"first"}""")
        call(screenRequest(body = """{"params":{"id_aph":"1"}}""")).close()

        network = noSignal
        val failure = runCatching {
            call(screenRequest(body = """{"params":{"id_aph":"2"}}"""))
        }

        assertEquals(IOException::class, failure.exceptionOrNull()!!::class)
    }

    @Test
    fun `an error answer is not kept as the screen`() {
        network = answer("""{"error":"boom"}""", code = 500)
        call(screenRequest()).close()

        network = noSignal
        val failure = runCatching { call(screenRequest()) }

        assertEquals(IOException::class, failure.exceptionOrNull()!!::class)
    }

    @Test
    fun `requests that are not screens are left alone`() {
        network = answer("""{}""")
        call(screenRequest(path = "aph")).close()

        network = noSignal
        val failure = runCatching { call(screenRequest(path = "aph")) }

        assertEquals(IOException::class, failure.exceptionOrNull()!!::class)
    }

    @Test
    fun `a copy that cannot be written does not cost the crew the screen`() {
        val brokenDisk = object : BlobCipher {
            override fun encrypt(plaintext: ByteArray): ByteArray = error("keystore down")
            override fun decrypt(sealed: ByteArray): ByteArray = sealed
        }
        client = clientWith(ScreenCacheStore(folder.newFolder("broken"), brokenDisk))
        network = answer("""{"body":["aph"]}""")

        val response = call(screenRequest())

        assertEquals("""{"body":["aph"]}""", response.body!!.string())
    }
}
