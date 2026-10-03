package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.commons.security.BlobCipher
import com.skgtecnologia.sisem.data.offline.screen.ReversingCipher
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException

private val JSON = "application/json".toMediaType()

private interface FakeAphApi {
    @POST("aph")
    suspend fun sendMedicalHistory(@Body body: RequestBody): retrofit2.Response<Unit>
}

/**
 * Runs the interceptor in a real OkHttp client, after a stand-in for the token interceptor and
 * before a stand-in for the network.
 */
class OutboxInterceptorTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val connectivity = MutableStateFlow(Connectivity.ONLINE)
    private val networkMonitor = object : NetworkMonitor {
        override val connectivity: StateFlow<Connectivity> = this@OutboxInterceptorTest.connectivity
    }

    private val dao = FakeOutboxDao()
    private val authRepository = mockk<AuthRepository>()
    private var scheduled = 0
    private val sent = mutableListOf<Request>()
    private var network: (Request) -> Response = { request -> answer(request, 200) }
    private var signedAs: String? = "token-aux1"

    private lateinit var client: OkHttpClient

    @Before
    fun setup() {
        coEvery { authRepository.getAllAccessTokens() } returns
            listOf(token("aux1"), token("medic1", role = "medic_aph"))
        client = clientWith(ReversingCipher())
    }

    private fun clientWith(cipher: BlobCipher): OkHttpClient {
        val store = OutboxStore(dao, folder.root.resolve("outbox"), cipher)
        return OkHttpClient.Builder()
            .addInterceptor(
                Interceptor { chain ->
                    val token = signedAs ?: return@Interceptor chain.proceed(chain.request())
                    chain.proceed(chain.request().newBuilder().header(AUTHORIZATION_HEADER, token.asBearer()).build())
                }
            )
            .addInterceptor(OutboxInterceptor(store, { scheduled++ }, networkMonitor, authRepository))
            .addInterceptor(
                Interceptor { chain ->
                    sent += chain.request()
                    network(chain.request())
                }
            )
            .build()
    }

    private fun answer(request: Request, code: Int) = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(code)
        .message("x")
        .body("".toResponseBody(JSON))
        .build()

    private fun post(path: String, body: String = """{"history":1}""") =
        Request.Builder().url(BASE + path).post(body.toRequestBody(JSON)).build()

    private fun call(request: Request) = client.newCall(request).execute()

    @Test
    fun `with signal a write goes straight out, stamped`() {
        val response = call(post("aph"))

        assertEquals(200, response.code)
        assertNotNull(sent.single().header(IDEMPOTENCY_KEY_HEADER))
        assertNotNull(sent.single().header(CLIENT_CREATED_AT_HEADER))
        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun `without signal a write is kept, accepted and the sync scheduled`() {
        connectivity.value = Connectivity.OFFLINE

        val response = call(post("aph"))

        assertEquals(202, response.code)
        assertTrue(sent.isEmpty())
        assertEquals("aux1", dao.rows.value.single().createdBy)
        assertEquals(1, scheduled)
    }

    @Test
    fun `a write that fails on the way is kept with the identity it was sent with`() {
        network = { throw IOException("timeout") }

        val response = call(post("aph"))

        assertEquals(202, response.code)
        assertEquals(sent.single().header(IDEMPOTENCY_KEY_HEADER), dao.rows.value.single().idempotencyKey)
    }

    @Test
    fun `once something is queued, later writes queue behind it even with signal`() {
        connectivity.value = Connectivity.OFFLINE
        call(post("aph"))
        connectivity.value = Connectivity.ONLINE

        call(post("aph/files"))

        assertTrue(sent.isEmpty())
        assertEquals(listOf(BASE + "aph", BASE + "aph/files"), dao.rows.value.map { it.url })
    }

    @Test
    fun `a write that cannot be kept is never reported as accepted`() {
        client = clientWith(
            object : BlobCipher {
                override fun encrypt(plaintext: ByteArray): ByteArray = throw IOException("disk full")
                override fun decrypt(sealed: ByteArray): ByteArray = sealed
            }
        )
        connectivity.value = Connectivity.OFFLINE

        val result = runCatching { call(post("aph")) }

        assertTrue(result.exceptionOrNull() is IOException)
        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun `the creator is whoever signed the write, not whoever holds the role`() {
        signedAs = "token-medic1"
        connectivity.value = Connectivity.OFFLINE

        call(post("aph"))

        assertEquals("medic1", dao.rows.value.single().createdBy)
    }

    @Test
    fun `a write signed by nobody is kept without a creator`() {
        signedAs = null
        connectivity.value = Connectivity.OFFLINE

        call(post("aph"))

        assertNull(dao.rows.value.single().createdBy)
    }

    @Test
    fun `screens and replays are left alone`() {
        connectivity.value = Connectivity.OFFLINE
        network = { throw IOException("no signal") }

        assertTrue(runCatching { call(post("screen/aph")) }.isFailure)
        assertTrue(
            runCatching { call(post("aph").newBuilder().header(OUTBOX_REPLAY_HEADER, "r1").build()) }.isFailure
        )
        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun `a queued write reads as a success through Retrofit`() = runBlocking {
        connectivity.value = Connectivity.OFFLINE
        val api = Retrofit.Builder().baseUrl(BASE).client(client).build().create(FakeAphApi::class.java)

        val response = api.sendMedicalHistory("""{"history":1}""".toRequestBody(JSON))

        // What NetworkApi.apiCall checks before handing the result to the use case.
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
    }
}
