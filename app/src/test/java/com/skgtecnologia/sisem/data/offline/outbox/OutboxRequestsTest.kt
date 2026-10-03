package com.skgtecnologia.sisem.data.offline.outbox

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class OutboxRequestsTest {

    private fun post(path: String) =
        Request.Builder().url(BASE + path).post("{}".toRequestBody("application/json".toMediaType())).build()

    @Test
    fun `the writes a crew makes are queueable`() {
        listOf(
            "aph",
            "aph/files",
            "aph/delete-file",
            "aph/save-stretcher-retention",
            "aph/send-mail",
            "preoperational/driver",
            "novelty/123/assistant",
            "transfer-return",
            "crew"
        ).forEach { path -> assertTrue(path, post(path).isQueueableWrite()) }
    }

    @Test
    fun `screens, sign-in and reads are never queued`() {
        listOf("screen/aph", "screen/preoperational/driver", "authentication", "device", "aph/123")
            .forEach { path -> assertFalse(path, post(path).isQueueableWrite()) }

        assertFalse(Request.Builder().url(BASE + "aph").get().build().isQueueableWrite())
    }

    @Test
    fun `stamping keeps an identity the write already has`() {
        val first = post("aph").stamped(Instant.parse("2026-10-02T08:00:00Z"))
        val again = first.stamped(Instant.parse("2026-10-02T12:00:00Z"))

        assertEquals(first.header(IDEMPOTENCY_KEY_HEADER), again.header(IDEMPOTENCY_KEY_HEADER))
        assertEquals("2026-10-02T08:00:00Z", again.header(CLIENT_CREATED_AT_HEADER))
    }

    @Test
    fun `the token is never stored with the headers`() {
        val request = post("aph").newBuilder()
            .header(AUTHORIZATION_HEADER, "Bearer secret")
            .header("geolocation", "4.6, -74.1")
            .build()

        val stored = request.headers.serialized()

        assertFalse(stored.contains("secret"))
        assertEquals("4.6, -74.1", stored.deserializedHeaders()["geolocation"])
    }

    @Test
    fun `a replay is rebuilt unsigned, marked, and with its body`() {
        val entry = OutboxEntity(
            requestId = "r1",
            idempotencyKey = "k1",
            method = "POST",
            url = BASE + "aph",
            headers = "$IDEMPOTENCY_KEY_HEADER: k1",
            contentType = "application/json; charset=utf-8",
            hasBody = true,
            createdBy = "aux1",
            createdAt = 0
        )

        val replay = entry.toReplayRequest("""{"a":1}""".toByteArray())

        assertTrue(replay.isOutboxReplay())
        assertNull(replay.header(AUTHORIZATION_HEADER))
        assertEquals("k1", replay.header(IDEMPOTENCY_KEY_HEADER))
        assertEquals("""{"a":1}""", replay.bodyBytes()?.decodeToString())
        assertEquals("application/json; charset=utf-8", replay.body?.contentType().toString())
    }
}
