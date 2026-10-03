package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.data.offline.screen.ReversingCipher
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.IOException

private val JSON = "application/json".toMediaType()

class OutboxSenderTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val dao = FakeOutboxDao()
    private val store by lazy { OutboxStore(dao, folder.root.resolve("outbox"), ReversingCipher()) }
    private val authRepository = mockk<AuthRepository>()
    private val sent = mutableListOf<Request>()
    private var network: (Request) -> Int = { 200 }

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            Interceptor { chain ->
                val request = chain.request()
                sent += request
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(network(request))
                    .message("x")
                    .body("""{"message":"bad"}""".toResponseBody(JSON))
                    .build()
            }
        )
        .build()

    private val sender by lazy { OutboxSender(store, OutboxSigner(authRepository) { NOW }, client) }

    init {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(token("aux1"), token("medic1"))
    }

    private suspend fun queue(path: String, createdBy: String?) = store.enqueue(
        Request.Builder().url(BASE + path).post(path.toRequestBody(JSON)).build().stamped(),
        createdBy
    )

    private fun Request.path() = url.encodedPath.removePrefix("/sisem-api/v1/")

    @Test
    fun `everything goes out in order, signed as its creator, and leaves the queue`() = runTest {
        queue("aph", "aux1")
        queue("aph/files", "aux1")
        queue("novelty/1/doctor", "medic1")

        assertEquals(DrainResult.DONE, sender.drain())

        assertEquals(listOf("aph", "aph/files", "novelty/1/doctor"), sent.map { it.path() })
        assertEquals(listOf("token-aux1", "token-aux1", "token-medic1"), sent.map { it.bearerToken() })
        assertTrue(sent.all { it.isOutboxReplay() })
        assertTrue(dao.rows.value.isEmpty())
    }

    @Test
    fun `a write the server refuses is set aside and the rest still go`() = runTest {
        queue("aph", "aux1")
        queue("aph/files", "aux1")
        network = { if (it.path() == "aph") 400 else 200 }

        assertEquals(DrainResult.DONE, sender.drain())

        val left = dao.rows.value.single()
        assertEquals(OutboxEntity.STATE_REJECTED, left.state)
        assertTrue(left.lastError.orEmpty().contains("bad"))
    }

    @Test
    fun `when a creator cannot sign, only their later writes wait`() = runTest {
        queue("aph", "aux1")
        queue("novelty/1/doctor", "medic1")
        queue("aph/files", "aux1")
        network = { if (it.bearerToken() == "token-aux1") 401 else 200 }

        assertEquals(DrainResult.TRY_AGAIN, sender.drain())

        // aux1's photos must not overtake the history that is waiting.
        assertEquals(listOf("aph", "novelty/1/doctor"), sent.map { it.path() })
        assertEquals(listOf("aph", "aph/files"), dao.rows.value.map { it.url.removePrefix(BASE) })
    }

    @Test
    fun `a creator who is no longer signed in holds their writes without sending anything`() = runTest {
        coEvery { authRepository.getAllAccessTokens() } returns listOf(token("aux2"))
        queue("aph", "aux1")

        assertEquals(DrainResult.TRY_AGAIN, sender.drain())

        assertTrue(sent.isEmpty())
        assertEquals(1, dao.rows.value.single().attempts)
    }

    @Test
    fun `a server that is down stops the pass and keeps everything`() = runTest {
        queue("aph", "aux1")
        queue("novelty/1/doctor", "medic1")
        network = { 503 }

        assertEquals(DrainResult.TRY_AGAIN, sender.drain())

        assertEquals(1, sent.size)
        assertEquals(2, dao.rows.value.count { it.state == OutboxEntity.STATE_PENDING })
    }

    @Test
    fun `no signal stops the pass and keeps everything`() = runTest {
        val offlineSender = OutboxSender(
            store,
            OutboxSigner(authRepository) { NOW },
            OkHttpClient.Builder().addInterceptor(Interceptor { throw IOException("no route") }).build()
        )
        queue("aph", "aux1")

        assertEquals(DrainResult.TRY_AGAIN, offlineSender.drain())
        assertEquals(OutboxEntity.STATE_PENDING, dao.rows.value.single().state)
    }

    @Test
    fun `a write made with nobody signed in is set aside`() = runTest {
        queue("aph", null)

        assertEquals(DrainResult.DONE, sender.drain())

        assertTrue(sent.isEmpty())
        assertEquals(OutboxEntity.STATE_REJECTED, dao.rows.value.single().state)
    }
}
