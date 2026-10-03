package com.skgtecnologia.sisem.data.offline.outbox

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.skgtecnologia.sisem.data.offline.outbox.OutboxEntity.Companion.STATE_PENDING
import com.skgtecnologia.sisem.data.offline.outbox.OutboxEntity.Companion.STATE_REJECTED
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboxDao {

    @Insert
    suspend fun insert(entity: OutboxEntity): Long

    @Query("SELECT * FROM outbox WHERE state = '$STATE_PENDING' ORDER BY sequence")
    suspend fun pendingInOrder(): List<OutboxEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM outbox WHERE state = '$STATE_PENDING')")
    suspend fun hasPending(): Boolean

    @Query("SELECT COUNT(*) FROM outbox WHERE state = '$STATE_PENDING'")
    fun observePendingCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM outbox WHERE state = '$STATE_PENDING' AND createdBy = :username")
    suspend fun countPendingBy(username: String): Int

    @Query("SELECT COUNT(*) FROM outbox WHERE state = '$STATE_REJECTED'")
    fun observeRejectedCount(): Flow<Int>

    @Query("DELETE FROM outbox WHERE sequence = :sequence")
    suspend fun delete(sequence: Long)

    @Query(
        "UPDATE outbox SET attempts = attempts + 1, lastError = :error, lastAttemptAt = :at " +
            "WHERE sequence = :sequence"
    )
    suspend fun recordAttempt(sequence: Long, error: String, at: Long)

    @Query(
        "UPDATE outbox SET state = '$STATE_REJECTED', attempts = attempts + 1, " +
            "lastError = :error, lastAttemptAt = :at WHERE sequence = :sequence"
    )
    suspend fun reject(sequence: Long, error: String, at: Long)
}
