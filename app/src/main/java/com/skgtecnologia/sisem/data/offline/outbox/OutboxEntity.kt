package com.skgtecnologia.sisem.data.offline.outbox

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A write that could not reach the server and is waiting to be sent again.
 *
 * Only metadata lives here. The body — a medical history, a photo — is a separate encrypted file
 * named after [requestId], because a photo would not fit in a database row and none of it should
 * sit in the database in the clear.
 */
@Entity(tableName = "outbox", indices = [Index(value = ["requestId"], unique = true)])
data class OutboxEntity(
    // Sending order. A sequence rather than createdAt, so two writes queued in the same
    // millisecond still go out in the order they were made: the photos of a medical history must
    // never reach the server before the history itself.
    @PrimaryKey(autoGenerate = true) val sequence: Long = 0,
    val requestId: String,
    val idempotencyKey: String,
    val method: String,
    val url: String,
    // Header lines as sent, minus Authorization: a token is a credential, and it would be
    // expired by the time this goes out anyway.
    val headers: String,
    val contentType: String?,
    val hasBody: Boolean,
    // Whose session signed the write. It is only ever sent again as that same user, never with
    // whichever crew member happens to be signed in on that role later.
    val createdBy: String?,
    val createdAt: Long,
    val attempts: Int = 0,
    val state: String = STATE_PENDING,
    val lastError: String? = null,
    val lastAttemptAt: Long? = null
) {
    companion object {
        const val STATE_PENDING = "PENDING"

        // The server answered and refused it. Sending it again would get the same answer, so it is
        // kept aside for someone to look at instead of blocking everything queued behind it.
        const val STATE_REJECTED = "REJECTED"
    }
}
