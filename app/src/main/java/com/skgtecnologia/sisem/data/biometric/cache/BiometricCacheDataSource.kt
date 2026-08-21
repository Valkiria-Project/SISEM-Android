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
                refreshToken = refreshToken.ifBlank { existing?.refreshToken.orEmpty() },
                embeddings = serialized,
                cloudSynced = false,
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
                createdAt = existing?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

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
