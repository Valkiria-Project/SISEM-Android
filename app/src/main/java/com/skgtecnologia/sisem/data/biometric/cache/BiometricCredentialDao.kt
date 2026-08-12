package com.skgtecnologia.sisem.data.biometric.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.biometric.cache.model.BiometricCredentialEntity

@Dao
interface BiometricCredentialDao {

    @Query("SELECT * FROM biometric_credentials WHERE username = :username")
    suspend fun getByUsername(username: String): BiometricCredentialEntity?

    /** Only usernames that have non-empty embeddings (i.e. have been enrolled). */
    @Query("SELECT username FROM biometric_credentials WHERE embeddings != ''")
    suspend fun enrolledUsernames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: BiometricCredentialEntity)

    @Query("DELETE FROM biometric_credentials WHERE username = :username")
    suspend fun deleteByUsername(username: String)

    @Query("DELETE FROM biometric_credentials")
    suspend fun clearAll()

    /** Records that have embeddings but have not yet been uploaded to the cloud. */
    @Query("SELECT * FROM biometric_credentials WHERE cloudSynced = 0 AND embeddings != ''")
    suspend fun getPendingSync(): List<BiometricCredentialEntity>

    @Query(
        "UPDATE biometric_credentials SET cloudSynced = :synced, updatedAt = :now WHERE username = :username"
    )
    suspend fun updateSyncStatus(
        username: String,
        synced: Boolean,
        now: Long = System.currentTimeMillis()
    )
}
