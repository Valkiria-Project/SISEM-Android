package com.skgtecnologia.sisem.data.offline

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skgtecnologia.sisem.data.offline.outbox.OutboxDao
import com.skgtecnologia.sisem.data.offline.outbox.OutboxEntity

/**
 * Kept apart from SisemDatabase on purpose. That one falls back to a destructive migration, so a
 * version bump without its migration silently wipes it — acceptable for cached data the server
 * can send again, not for medical histories that never reached the server. This database has no
 * such fallback: a missing migration fails loudly instead of deleting a patient's record.
 */
@Database(
    entities = [OutboxEntity::class],
    version = 1,
    exportSchema = true
)
abstract class OfflineDatabase : RoomDatabase() {

    abstract fun outboxDao(): OutboxDao
}
