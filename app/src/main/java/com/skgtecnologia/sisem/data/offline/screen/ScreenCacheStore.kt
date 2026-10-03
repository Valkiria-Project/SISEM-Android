package com.skgtecnologia.sisem.data.offline.screen

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.commons.security.BlobCipher
import timber.log.Timber
import java.io.DataInputStream
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

// Long enough to cover a shift and the day after it, short enough that a patient's history does
// not linger on a vehicle's device. Every screen is refreshed whenever it loads with signal, so
// this only bounds how old a copy can be when there is none.
private const val MAX_AGE_MILLIS = 72 * 60 * 60 * 1000L
private const val TIMESTAMP_BYTES = Long.SIZE_BYTES

/**
 * The last good copy of every server-driven screen, so a form still opens with no signal.
 *
 * Each entry is one file: when it was stored, then the encrypted response body. The time goes
 * inside the file rather than in its modification date because some devices silently ignore
 * setLastModified, and that date is what decides when patient data is deleted.
 */
class ScreenCacheStore(
    private val directory: File,
    private val cipher: BlobCipher,
    private val now: () -> Long = System::currentTimeMillis
) {

    // Purged on first use rather than on construction: the store is built while the network
    // client is, which can be on the main thread during startup, and first use never is.
    private val purged = AtomicBoolean(false)

    fun put(key: String, body: ByteArray) {
        purgeOnce()
        directory.mkdirs()
        val sealed = cipher.encrypt(body)
        val content = ByteBuffer.allocate(TIMESTAMP_BYTES + sealed.size)
            .putLong(now())
            .put(sealed)
            .array()

        // Write beside the entry and swap it in, so a crash mid-write leaves the previous copy
        // instead of a truncated one.
        val temporary = File(directory, "$key.tmp")
        temporary.writeBytes(content)
        if (!temporary.renameTo(entry(key))) {
            entry(key).delete()
            temporary.renameTo(entry(key))
        }
    }

    /** The stored body, or null when there is none, it expired, or it can no longer be opened. */
    fun get(key: String): ByteArray? {
        purgeOnce()
        val file = entry(key).takeIf { it.exists() } ?: return null

        val content = file.readBytes()
        val stored = content.storedAt()

        return if (stored == null || now() - stored > MAX_AGE_MILLIS) {
            file.delete()
            null
        } else {
            content.opened(file)
        }
    }

    private fun ByteArray.opened(file: File): ByteArray? =
        resultOf { cipher.decrypt(copyOfRange(TIMESTAMP_BYTES, size)) }
            .getOrElse { error ->
                // Typically the Keystore key is gone (app data cleared, device restored). The copy
                // is unreadable for good, so drop it rather than failing on it every time.
                Timber.w(error, "Discarding unreadable cached screen")
                file.delete()
                null
            }

    /** Deletes every expired entry, so old patient data goes even for screens never reopened. */
    fun purgeExpired() {
        directory.listFiles()?.forEach { file ->
            val stored = if (file.extension == "bin") file.readStoredAt() else null
            if (stored == null || now() - stored > MAX_AGE_MILLIS) file.delete()
        }
    }

    private fun purgeOnce() {
        if (purged.compareAndSet(false, true)) purgeExpired()
    }

    // Only the leading timestamp, so purging never reads or decrypts a body it is about to drop.
    private fun File.readStoredAt(): Long? = resultOf {
        DataInputStream(inputStream()).use { it.readLong() }
    }.getOrNull()

    private fun entry(key: String) = File(directory, "$key.bin")

    private fun ByteArray.storedAt(): Long? =
        if (size > TIMESTAMP_BYTES) ByteBuffer.wrap(this, 0, TIMESTAMP_BYTES).long else null
}
