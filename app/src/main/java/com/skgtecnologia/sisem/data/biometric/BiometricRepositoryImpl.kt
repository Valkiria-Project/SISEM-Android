package com.skgtecnologia.sisem.data.biometric

import com.skgtecnologia.sisem.commons.security.CredentialCipher
import com.skgtecnologia.sisem.data.biometric.cache.BiometricCacheDataSource
import com.skgtecnologia.sisem.data.biometric.remote.BiometricRemoteDataSource
import com.skgtecnologia.sisem.data.biometric.remote.BiometricSerializer
import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import com.skgtecnologia.sisem.domain.biometric.model.BiometricRegistrationStatus
import com.skgtecnologia.sisem.domain.biometric.model.LoginCredentials
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("TooManyFunctions")
class BiometricRepositoryImpl @Inject constructor(
    private val cacheDataSource: BiometricCacheDataSource,
    private val remoteDataSource: BiometricRemoteDataSource,
    private val credentialCipher: CredentialCipher
) : BiometricRepository {

    override suspend fun storeLocal(
        username: String,
        role: String,
        refreshToken: String,
        embeddings: List<FloatArray>
    ) = cacheDataSource.storeEmbeddings(username, embeddings, role, refreshToken)

    override suspend fun storeFromCloud(
        username: String,
        role: String,
        documentNumber: String,
        embeddings: List<FloatArray>
    ) = cacheDataSource.storeFromCloud(username, role, documentNumber, embeddings)

    override suspend fun getEmbeddings(username: String): List<FloatArray> =
        cacheDataSource.getEmbeddings(username)

    override suspend fun hasEmbedding(username: String): Boolean =
        cacheDataSource.hasEmbedding(username)

    override suspend fun enrolledUsernames(): List<String> =
        cacheDataSource.enrolledUsernames()

    override suspend fun getRole(username: String): String? =
        cacheDataSource.getRole(username)

    override suspend fun getRefreshToken(username: String): String? =
        cacheDataSource.getRefreshToken(username)

    override suspend fun updateMeta(username: String, role: String?, refreshToken: String?) =
        cacheDataSource.upsertMeta(username, role, refreshToken)

    override suspend fun clearUser(username: String) = cacheDataSource.clearUser(username)

    override suspend fun clearAll() = cacheDataSource.clearAll()

    override suspend fun storeLoginCredentials(username: String, password: String) {
        val encrypted = credentialCipher.encrypt(password)
        cacheDataSource.storeCredentials(username, encrypted.ciphertext, encrypted.iv)
    }

    override suspend fun getLoginCredentials(username: String): LoginCredentials? {
        val entity = cacheDataSource.getCredentials(username) ?: return null
        if (entity.encryptedPassword.isBlank() || entity.credentialIv.isBlank()) return null
        val password = runCatching {
            credentialCipher.decrypt(entity.encryptedPassword, entity.credentialIv)
        }.getOrNull() ?: return null
        return LoginCredentials(username = username, password = password)
    }

    override suspend fun purgeStale(maxIdleDays: Int) {
        val cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(maxIdleDays.toLong())
        cacheDataSource.deleteStale(cutoff)
    }

    override suspend fun uploadToCloud(username: String): Result<Unit> {
        val embeddings = cacheDataSource.getEmbeddings(username)
        val role = cacheDataSource.getRole(username).orEmpty()
        return remoteDataSource.upload(username, role, embeddings).also { result ->
            if (result.isSuccess) cacheDataSource.markSynced(username)
        }
    }

    override suspend fun uploadByDocument(
        document: String,
        embeddings: List<FloatArray>
    ): Result<Unit> = remoteDataSource.uploadByDocument(document, embeddings)

    override suspend fun storeFromLogin(
        username: String,
        role: String,
        documentNumber: String,
        embeddings: List<String>
    ) = cacheDataSource.storeFromCloud(
        username = username,
        role = role,
        documentNumber = documentNumber,
        embeddings = embeddings.map { BiometricSerializer.base64ToFloatArray(it) }
    )

    override suspend fun registrationStatus(documentNumber: String): BiometricRegistrationStatus =
        remoteDataSource.exists(documentNumber)

    override suspend fun getPendingSync(): List<String> =
        cacheDataSource.getPendingSync().map { it.username }

    override suspend fun markCloudSynced(username: String) =
        cacheDataSource.markSynced(username)
}
