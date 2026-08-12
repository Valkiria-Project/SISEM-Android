package com.skgtecnologia.sisem.domain.biometric

import com.skgtecnologia.sisem.domain.biometric.model.BiometricModel

@Suppress("TooManyFunctions")
interface BiometricRepository {
    // ── Local ────────────────────────────────────────────────────────────────
    suspend fun storeLocal(username: String, role: String, refreshToken: String, embeddings: List<FloatArray>)
    suspend fun storeFromCloud(username: String, role: String, embeddings: List<FloatArray>)
    suspend fun getEmbeddings(username: String): List<FloatArray>
    suspend fun hasEmbedding(username: String): Boolean
    suspend fun enrolledUsernames(): List<String>
    suspend fun getRole(username: String): String?
    suspend fun getRefreshToken(username: String): String?
    suspend fun updateMeta(username: String, role: String? = null, refreshToken: String? = null)
    suspend fun clearUser(username: String)
    suspend fun clearAll()

    // ── Cloud sync ───────────────────────────────────────────────────────────
    suspend fun uploadToCloud(username: String): Result<Unit>
    suspend fun fetchFromCloud(username: String): BiometricModel?
    suspend fun getPendingSync(): List<String>
    suspend fun markCloudSynced(username: String)
}
