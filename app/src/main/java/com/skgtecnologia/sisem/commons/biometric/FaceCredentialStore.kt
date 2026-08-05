package com.skgtecnologia.sisem.commons.biometric

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS_NAME = "face_credentials"
private const val DUMP_SAMPLE_SIZE = 8
private const val PREFIX_EMBEDDINGS = "emb_" // stores multiple comma-separated vectors
private const val PREFIX_TOKEN = "tok_"
private const val PREFIX_ROLE = "role_"
private const val EMBEDDING_SEPARATOR = "|" // separates individual embedding vectors

/**
 * Encrypted on-device store for face data and refresh tokens.
 *
 * Security notes:
 * - Uses EncryptedSharedPreferences backed by Android Keystore (AES-256-GCM).
 * - The Keystore key is hardware-backed on supported devices.
 * - Biometric data (embeddings) never leaves the device.
 * - Multiple embeddings per user improve verification accuracy.
 */
@Suppress("TooManyFunctions")
@Singleton
class FaceCredentialStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // ── Face embeddings (multiple angles) ───────────────────────────────────

    /** Stores multiple embeddings (one per capture angle) for a user. */
    fun storeEmbeddings(username: String, embeddings: List<FloatArray>) {
        val serialized = embeddings.joinToString(EMBEDDING_SEPARATOR) { it.joinToString(",") }
        prefs.edit().putString(PREFIX_EMBEDDINGS + username, serialized).apply()
    }

    /** Returns all stored embeddings for a user, or empty list if none. */
    fun getEmbeddings(username: String): List<FloatArray> {
        val raw = prefs.getString(PREFIX_EMBEDDINGS + username, null) ?: return emptyList()
        return raw.split(EMBEDDING_SEPARATOR).mapNotNull { vectorStr ->
            runCatching { vectorStr.split(",").map { it.toFloat() }.toFloatArray() }.getOrNull()
        }
    }

    fun hasEmbedding(username: String): Boolean =
        prefs.contains(PREFIX_EMBEDDINGS + username)

    fun enrolledUsernames(): List<String> =
        prefs.all.keys
            .filter { it.startsWith(PREFIX_EMBEDDINGS) }
            .map { it.removePrefix(PREFIX_EMBEDDINGS) }

    // ── Role (for post-logout security check) ───────────────────────────────

    fun storeRole(username: String, role: String) {
        prefs.edit().putString(PREFIX_ROLE + username, role).apply()
    }

    fun getRole(username: String): String? =
        prefs.getString(PREFIX_ROLE + username, null)

    // ── Refresh tokens ───────────────────────────────────────────────────────

    fun storeRefreshToken(username: String, refreshToken: String) {
        prefs.edit().putString(PREFIX_TOKEN + username, refreshToken).apply()
    }

    fun getRefreshToken(username: String): String? =
        prefs.getString(PREFIX_TOKEN + username, null)

    // ── Cleanup ──────────────────────────────────────────────────────────────

    fun clearUser(username: String) {
        prefs.edit()
            .remove(PREFIX_EMBEDDINGS + username)
            .remove(PREFIX_TOKEN + username)
            .remove(PREFIX_ROLE + username)
            .apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    /**
     * Logs a full diagnostic dump of all stored biometric data.
     * Output goes to Timber → FileLoggingTree → Downloads/SISEM-Logs/
     * Useful to verify what is being stored and detect similarity issues.
     */
    @Suppress("TooManyFunctions", "NestedBlockDepth")
    fun dumpToLog() {
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

    private fun dumpUser(username: String) {
        val embeddings = getEmbeddings(username)
        val role = getRole(username) ?: "unknown"
        Timber.d("[FaceStore] User=$username role=$role angles=${embeddings.size}")
        embeddings.forEachIndexed { idx, emb ->
            Timber.d(
                "[FaceStore]   angle[$idx] features=${emb.size} " +
                    "first8=${emb.take(DUMP_SAMPLE_SIZE).joinToString { "%.4f".format(it) }}"
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

    private fun dumpCrossUserSimilarity(users: List<String>) {
        Timber.d("[FaceStore] ----- Cross-user similarity -----")
        for (i in users.indices) {
            for (j in i + 1 until users.size) {
                val embsI = getEmbeddings(users[i])
                val embsJ = getEmbeddings(users[j])
                val maxSim = embsI.flatMap { a ->
                    embsJ.map { b -> FaceEmbeddingHelper.similarity(a, b) }
                }.maxOrNull() ?: 0f
                Timber.d(
                    "[FaceStore]   ${users[i]} vs ${users[j]} " +
                        "maxSim=${"%.4f".format(maxSim)} " +
                        "threshold=${FaceEmbeddingHelper.MATCH_THRESHOLD}"
                )
            }
        }
    }
}
