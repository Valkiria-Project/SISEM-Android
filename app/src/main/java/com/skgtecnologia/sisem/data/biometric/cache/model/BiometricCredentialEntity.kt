package com.skgtecnologia.sisem.data.biometric.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local persistent store for on-device face embeddings.
 *
 * [embeddings] — face-recognition vectors from the 3 enrollment angles, serialized as
 *     angle1_f1,f2,...,f512|angle2_f1,...|angle3_f1,...  (pipe-separated angles, comma-separated floats)
 * [cloudSynced] — true when the embeddings have been successfully uploaded to the backend,
 *     allowing the user to authenticate on any device without re-enrolling.
 * [encryptedPassword]/[credentialIv] — AES/GCM ciphertext (and its IV) of the crew member's
 *     login password, so biometric login can silently re-authenticate. The key lives in the
 *     Android Keystore; only the ciphertext is stored here.
 * [lastLoginAt] — epoch millis of the last successful login, refreshed on every login. Records
 *     idle for more than the inactivity window are purged (record + encrypted credentials).
 */
@Entity(tableName = "biometric_credentials")
data class BiometricCredentialEntity(
    @PrimaryKey val username: String,
    val role: String,
    val documentNumber: String = "",
    val refreshToken: String,
    val embeddings: String,
    val cloudSynced: Boolean = false,
    val encryptedPassword: String = "",
    val credentialIv: String = "",
    val lastLoginAt: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
