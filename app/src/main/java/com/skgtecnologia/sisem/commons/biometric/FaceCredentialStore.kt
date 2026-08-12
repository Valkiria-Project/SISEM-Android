package com.skgtecnologia.sisem.commons.biometric

import com.skgtecnologia.sisem.data.biometric.cache.BiometricCacheDataSource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val DUMP_SAMPLE_SIZE = 8

/**
 * Facade over [BiometricCacheDataSource] that keeps the same API surface used by ViewModels
 * and the access-token authenticator.  All methods are now suspend — callers that were
 * previously synchronous have been updated to run inside coroutine scopes.
 *
 * Security notes:
 * - Backed by Room (AES-256 encrypted at rest on devices that support it).
 * - Biometric embeddings never leave the device except via [BiometricRepository.uploadToCloud].
 */
@Singleton
@Suppress("TooManyFunctions")
class FaceCredentialStore @Inject constructor(
    private val cacheDataSource: BiometricCacheDataSource
) {

    suspend fun storeEmbeddings(username: String, embeddings: List<FloatArray>) =
        cacheDataSource.storeEmbeddings(username, embeddings, role = "", refreshToken = "")

    suspend fun getEmbeddings(username: String): List<FloatArray> =
        cacheDataSource.getEmbeddings(username)

    suspend fun hasEmbedding(username: String): Boolean =
        cacheDataSource.hasEmbedding(username)

    suspend fun enrolledUsernames(): List<String> =
        cacheDataSource.enrolledUsernames()

    suspend fun storeRole(username: String, role: String) =
        cacheDataSource.upsertMeta(username, role = role)

    suspend fun getRole(username: String): String? =
        cacheDataSource.getRole(username)

    suspend fun storeRefreshToken(username: String, refreshToken: String) =
        cacheDataSource.upsertMeta(username, refreshToken = refreshToken)

    suspend fun getRefreshToken(username: String): String? =
        cacheDataSource.getRefreshToken(username)

    suspend fun clearUser(username: String) = cacheDataSource.clearUser(username)

    suspend fun clearAll() = cacheDataSource.clearAll()

    suspend fun dumpToLog() {
        val users = enrolledUsernames()
        Timber.d("[FaceStore] ===== BIOMETRIC STORE DUMP (${users.size} users) =====")
        if (users.isEmpty()) {
            Timber.d("[FaceStore] No enrolled users.")
            return
        }
        users.forEach { username -> dumpUser(username) }
        if (users.size > 1) dumpCrossUserSimilarity(users)
        Timber.d("[FaceStore] ===== END DUMP =====")
    }

    private suspend fun dumpUser(username: String) {
        val embeddings = getEmbeddings(username)
        val role = getRole(username) ?: "unknown"
        Timber.d("[FaceStore] User=$username role=$role angles=${embeddings.size}")
        embeddings.forEachIndexed { idx, emb ->
            Timber.d(
                "[FaceStore]   angle[$idx] features=${emb.size} " +
                    "first$DUMP_SAMPLE_SIZE=${emb.take(DUMP_SAMPLE_SIZE).joinToString { "%.4f".format(it) }}"
            )
        }
        if (embeddings.size > 1) {
            for (i in embeddings.indices) {
                for (j in i + 1 until embeddings.size) {
                    val sim = FaceEmbeddingHelper.similarity(embeddings[i], embeddings[j])
                    Timber.d("[FaceStore]   intra-user sim[$i vs $j]=${"%.4f".format(sim)}")
                }
            }
        }
    }

    private suspend fun dumpCrossUserSimilarity(users: List<String>) {
        Timber.d("[FaceStore] ----- Cross-user similarity -----")
        for (i in users.indices) {
            for (j in i + 1 until users.size) {
                val embsI = getEmbeddings(users[i])
                val embsJ = getEmbeddings(users[j])
                val maxSim = embsI
                    .flatMap { a -> embsJ.map { b -> FaceEmbeddingHelper.similarity(a, b) } }
                    .maxOrNull() ?: 0f
                Timber.d(
                    "[FaceStore]   ${users[i]} vs ${users[j]} " +
                        "maxSim=${"%.4f".format(maxSim)} " +
                        "threshold=${FaceEmbeddingHelper.MATCH_THRESHOLD}"
                )
            }
        }
    }
}
