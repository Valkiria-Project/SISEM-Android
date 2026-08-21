package com.skgtecnologia.sisem.data.biometric.cache

import com.skgtecnologia.sisem.data.biometric.cache.model.BiometricCredentialEntity
import javax.inject.Inject

private const val ANGLE_SEPARATOR = "|"
private const val VALUE_SEPARATOR = ","

@Suppress("TooManyFunctions")
class BiometricCacheDataSource @Inject constructor(
    private val dao: BiometricCredentialDao
) {

    suspend fun getEmbeddings(username: String): List<FloatArray> {
        val raw = dao.getByUsername(username)?.embeddings?.takeIf { it.isNotBlank() }
            ?: return emptyList()
        return raw.split(ANGLE_SEPARATOR).mapNotNull { angle ->
            runCatching {
                angle.split(VALUE_SEPARATOR).map { it.toFloat() }.toFloatArray()
            }.getOrNull()
        }
    }

    suspend fun storeEmbeddings(
        username: String,
        embeddings: List<FloatArray>,
        role: String,
        refreshToken: String
    ) {
        val serialized = embeddings.joinToString(ANGLE_SEPARATOR) { it.joinToString(VALUE_SEPARATOR) }
        val existing = dao.getByUsername(username)
        dao.upsert(
            BiometricCredentialEntity(
                username = username,
                role = role.ifBlank { existing?.role.orEmpty() },
                documentNumber = existing?.documentNumber.orEmpty(),
                refreshToken = refreshToken.ifBlank { existing?.refreshToken.orEmpty() },
                embeddings = serialized,
                cloudSynced = false,
                encryptedPassword = existing?.encryptedPassword.orEmpty(),
                credentialIv = existing?.credentialIv.orEmpty(),
                lastLoginAt = existing?.lastLoginAt ?: 0L,
                createdAt = existing?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun storeFromCloud(
        username: String,
        role: String,
        documentNumber: String,
        embeddings: List<FloatArray>
    ) {
        val serialized = embeddings.joinToString(ANGLE_SEPARATOR) { it.joinToString(VALUE_SEPARATOR) }
        val existing = dao.getByUsername(username)
        dao.upsert(
            BiometricCredentialEntity(
                username = username,
                role = role.ifBlank { existing?.role.orEmpty() },
                documentNumber = documentNumber.ifBlank { existing?.documentNumber.orEmpty() },
                refreshToken = existing?.refreshToken.orEmpty(),
                embeddings = serialized,
                cloudSynced = true,
                encryptedPassword = existing?.encryptedPassword.orEmpty(),
                credentialIv = existing?.credentialIv.orEmpty(),
                lastLoginAt = existing?.lastLoginAt ?: 0L,
                createdAt = existing?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    /**
     * Persists the AES/GCM-encrypted login password and stamps [lastLoginAt] with now, so a
     * constant user keeps sliding out of the inactivity purge on every login. Embeddings and
     * other metadata are preserved.
     */
    suspend fun storeCredentials(username: String, encryptedPassword: String, iv: String) {
        val existing = dao.getByUsername(username)
        val now = System.currentTimeMillis()
        dao.upsert(
            BiometricCredentialEntity(
                username = username,
                role = existing?.role.orEmpty(),
                documentNumber = existing?.documentNumber.orEmpty(),
                refreshToken = existing?.refreshToken.orEmpty(),
                embeddings = existing?.embeddings.orEmpty(),
                cloudSynced = existing?.cloudSynced ?: false,
                encryptedPassword = encryptedPassword,
                credentialIv = iv,
                lastLoginAt = now,
                createdAt = existing?.createdAt ?: now,
                updatedAt = now
            )
        )
    }

    suspend fun getCredentials(username: String): BiometricCredentialEntity? =
        dao.getByUsername(username)

    suspend fun deleteStale(cutoff: Long) = dao.deleteStale(cutoff)

    suspend fun upsertMeta(username: String, role: String? = null, refreshToken: String? = null) {
        val existing = dao.getByUsername(username)
        dao.upsert(
            if (existing != null) {
                existing.copy(
                    role = role ?: existing.role,
                    refreshToken = refreshToken ?: existing.refreshToken,
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                BiometricCredentialEntity(
                    username = username,
                    role = role.orEmpty(),
                    refreshToken = refreshToken.orEmpty(),
                    embeddings = ""
                )
            }
        )
    }

    suspend fun hasEmbedding(username: String): Boolean {
        val entity = dao.getByUsername(username)
        return entity != null && entity.embeddings.isNotBlank()
    }

    suspend fun enrolledUsernames(): List<String> = dao.enrolledUsernames()

    suspend fun getRole(username: String): String? = dao.getByUsername(username)?.role

    suspend fun getRefreshToken(username: String): String? =
        dao.getByUsername(username)?.refreshToken

    suspend fun clearUser(username: String) = dao.deleteByUsername(username)

    suspend fun clearAll() = dao.clearAll()

    suspend fun getPendingSync(): List<BiometricCredentialEntity> = dao.getPendingSync()

    suspend fun markSynced(username: String) = dao.updateSyncStatus(username, synced = true)
}
