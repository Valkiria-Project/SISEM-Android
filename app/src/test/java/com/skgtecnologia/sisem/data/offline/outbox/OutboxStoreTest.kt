package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.data.offline.screen.LostKeyCipher
import com.skgtecnologia.sisem.data.offline.screen.ReversingCipher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class OutboxStoreTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val dao = FakeOutboxDao()

    private fun write(body: String) = Request.Builder()
        .url(BASE + "aph")
        .post(body.toRequestBody("application/json".toMediaType()))
        .build()
        .stamped()

    private fun storeIn(bodies: File, cipher: com.skgtecnologia.sisem.commons.security.BlobCipher = ReversingCipher()) =
        OutboxStore(dao, bodies, cipher)

    @Test
    fun `writes come back in the order they were made, bodies intact`() = runTest {
        val store = storeIn(folder.newFolder("outbox"))

        store.enqueue(write("""{"history":1}"""), createdBy = "aux1")
        store.enqueue(write("""{"photos":1}"""), createdBy = "aux1")

        val pending = store.pending()
        assertEquals(listOf("""{"history":1}""", """{"photos":1}"""), pending.map { it.body?.decodeToString() })
        assertEquals("aux1", pending.first().entry.createdBy)
    }

    @Test
    fun `a body never reaches the disk in the clear`() = runTest {
        val bodies = folder.newFolder("outbox")

        storeIn(bodies).enqueue(write("""{"patient":"Ana"}"""), createdBy = "aux1")

        val onDisk = bodies.listFiles().orEmpty().single().readBytes().decodeToString()
        assertFalse(onDisk.contains("Ana"))
    }

    @Test
    fun `a write without an idempotency key is refused and nothing is kept`() = runTest {
        val store = storeIn(folder.newFolder("outbox"))
        val unstamped = Request.Builder().url(BASE + "aph").post("{}".toRequestBody(null)).build()

        val result = runCatching { store.enqueue(unstamped, createdBy = "aux1") }

        assertTrue(result.isFailure)
        assertFalse(store.hasPending())
    }

    @Test
    fun `a body whose key is gone is reported, not dropped`() = runTest {
        val bodies = folder.newFolder("outbox")
        storeIn(bodies).enqueue(write("{}"), createdBy = "aux1")

        val pending = storeIn(bodies, LostKeyCipher()).pending().single()

        assertNotNull(pending.unreadable)
    }

    @Test
    fun `a sent write is gone, a rejected one keeps its body for support`() = runTest {
        val bodies = folder.newFolder("outbox")
        val store = storeIn(bodies)
        store.enqueue(write("""{"sent":1}"""), createdBy = "aux1")
        store.enqueue(write("""{"refused":1}"""), createdBy = "aux1")
        val (sent, refused) = store.pending()

        store.complete(sent)
        store.reject(refused, "HTTP 400")

        assertFalse(store.hasPending())
        assertEquals(OutboxEntity.STATE_REJECTED, dao.rows.value.single().state)
        assertEquals(1, bodies.listFiles().orEmpty().size)
    }
}
