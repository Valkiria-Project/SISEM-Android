package com.skgtecnologia.sisem.data.biometric

import com.skgtecnologia.sisem.data.biometric.cache.BiometricCacheDataSource
import com.skgtecnologia.sisem.data.biometric.remote.BiometricRemoteDataSource
import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import com.skgtecnologia.sisem.domain.biometric.model.BiometricModel
import javax.inject.Inject

@Suppress("TooManyFunctions")
class BiometricRepositoryImpl @Inject constructor(
    private val cacheDataSource: BiometricCacheDataSource,
    private val remoteDataSource: BiometricRemoteDataSource
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

    override suspend fun fetchFromCloud(documentNumber: String): BiometricModel? =
        remoteDataSource.fetch(documentNumber)

    override suspend fun getPendingSync(): List<String> =
        cacheDataSource.getPendingSync().map { it.username }

    override suspend fun markCloudSynced(username: String) =
        cacheDataSource.markSynced(username)
}
