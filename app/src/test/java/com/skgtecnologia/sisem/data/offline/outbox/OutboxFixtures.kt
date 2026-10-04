package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

internal const val BASE = "https://api.example.test/sisem-api/v1/"
internal val NOW: LocalDateTime = LocalDateTime.of(2026, 10, 2, 8, 0)

/** The outbox table in memory, with the same ordering and state rules as the Room queries. */
internal class FakeOutboxDao : OutboxDao {

    val rows = MutableStateFlow<List<OutboxEntity>>(emptyList())
    private var nextSequence = 1L

    override suspend fun insert(entity: OutboxEntity): Long {
        check(rows.value.none { it.requestId == entity.requestId }) { "requestId is unique" }
        val stored = entity.copy(sequence = nextSequence++)
        rows.value = rows.value + stored
        return stored.sequence
    }

    override suspend fun pendingInOrder(): List<OutboxEntity> =
        rows.value.filter { it.state == OutboxEntity.STATE_PENDING }.sortedBy { it.sequence }

    override suspend fun hasPending(): Boolean = pendingInOrder().isNotEmpty()

    override fun observePendingCount(): Flow<Int> =
        rows.map { list -> list.count { it.state == OutboxEntity.STATE_PENDING } }

    override suspend fun countPendingBy(username: String): Int =
        pendingInOrder().count { it.createdBy == username }

    override fun observeRejectedCount(): Flow<Int> =
        rows.map { list -> list.count { it.state == OutboxEntity.STATE_REJECTED } }

    override suspend fun delete(sequence: Long) {
        rows.value = rows.value.filterNot { it.sequence == sequence }
    }

    override suspend fun recordAttempt(sequence: Long, error: String, at: Long) = update(sequence) {
        it.copy(attempts = it.attempts + 1, lastError = error, lastAttemptAt = at)
    }

    override suspend fun reject(sequence: Long, error: String, at: Long) = update(sequence) {
        it.copy(
            state = OutboxEntity.STATE_REJECTED,
            attempts = it.attempts + 1,
            lastError = error,
            lastAttemptAt = at
        )
    }

    private fun update(sequence: Long, change: (OutboxEntity) -> OutboxEntity) {
        rows.value = rows.value.map { if (it.sequence == sequence) change(it) else it }
    }
}

internal fun token(
    username: String,
    accessToken: String = "token-$username",
    role: String = "auxiliary_and_or_taph",
    expDate: LocalDateTime = NOW.plusMinutes(5)
) = AccessTokenModel(
    userId = username.hashCode(),
    dateTime = NOW,
    accessToken = accessToken,
    refreshToken = "refresh-$username",
    tokenType = "Bearer",
    username = username,
    role = role,
    isAdmin = false,
    nameUser = username,
    preoperational = null,
    turn = null,
    isWarning = false,
    docType = "CC",
    document = "1",
    refreshDateTime = NOW,
    expDate = expDate
)
