package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.commons.security.BlobCipher
import kotlinx.coroutines.flow.Flow
import okhttp3.Request
import java.io.File
import java.util.UUID

/** A queued write and its body, or the reason the body can no longer be read. */
data class QueuedWrite(
    val entry: OutboxEntity,
    val body: ByteArray?,
    val unreadable: Throwable? = null
) {
    // Arrays compare by reference by default; a queued write is identified by its entry.
    override fun equals(other: Any?): Boolean = other is QueuedWrite && other.entry == entry

    override fun hashCode(): Int = entry.hashCode()
}

/**
 * Writes waiting for the network, in the order they were made. Metadata goes to the offline
 * database and each body to its own encrypted file.
 */
class OutboxStore(
    private val dao: OutboxDao,
    private val bodies: File,
    private val cipher: BlobCipher,
    private val now: () -> Long = System::currentTimeMillis
) {

    /**
     * Keeps [request] to be sent later as [createdBy]. Throws when it cannot be kept, because a
     * write must never be reported as accepted unless it is safely on the device.
     */
    suspend fun enqueue(request: Request, createdBy: String?) {
        val requestId = UUID.randomUUID().toString()
        val body = request.bodyBytes()

        // Body first: a row without its body would be a record that can never be sent, while a
        // body without its row is only a stray file.
        if (body != null) {
            bodies.mkdirs()
            bodyFile(requestId).writeBytes(cipher.encrypt(body))
        }

        dao.insert(
            OutboxEntity(
                requestId = requestId,
                idempotencyKey = checkNotNull(request.header(IDEMPOTENCY_KEY_HEADER)) {
                    "A queued write needs an idempotency key, or a resend could duplicate it"
                },
                method = request.method,
                url = request.url.toString(),
                headers = request.headers.serialized(),
                contentType = request.body?.contentType()?.toString(),
                hasBody = body != null,
                createdBy = createdBy,
                createdAt = now()
            )
        )
    }

    suspend fun hasPending(): Boolean = dao.hasPending()

    /** Writes still waiting to be sent as [username], who is the only one they can be sent as. */
    suspend fun pendingCountFor(username: String): Int = dao.countPendingBy(username)

    /** Everything still waiting, oldest first, with its body opened. */
    suspend fun pending(): List<QueuedWrite> = dao.pendingInOrder().map { entry ->
        if (!entry.hasBody) {
            QueuedWrite(entry, body = null)
        } else {
            resultOf { cipher.decrypt(bodyFile(entry.requestId).readBytes()) }.fold(
                onSuccess = { QueuedWrite(entry, body = it) },
                onFailure = { QueuedWrite(entry, body = null, unreadable = it) }
            )
        }
    }

    suspend fun complete(write: QueuedWrite) {
        dao.delete(write.entry.sequence)
        bodyFile(write.entry.requestId).delete()
    }

    suspend fun retryLater(write: QueuedWrite, reason: String) {
        dao.recordAttempt(write.entry.sequence, reason, now())
    }

    /**
     * Sets the write aside for good. Its body is kept on purpose: it is a record the server never
     * stored, and support may still need to recover it.
     */
    suspend fun reject(write: QueuedWrite, reason: String) {
        dao.reject(write.entry.sequence, reason, now())
    }

    fun observePendingCount(): Flow<Int> = dao.observePendingCount()

    fun observeRejectedCount(): Flow<Int> = dao.observeRejectedCount()

    private fun bodyFile(requestId: String) = File(bodies, "$requestId.bin")
}
