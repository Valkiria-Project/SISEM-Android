package com.skgtecnologia.sisem.domain.auth

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun authenticate(username: String, password: String): AccessTokenModel

    suspend fun refreshToken(currentToken: AccessTokenModel): AccessTokenModel

    suspend fun getLastToken(): String?

    suspend fun observeCurrentAccessToken(): Flow<AccessTokenModel?>

    suspend fun getAllAccessTokens(): List<AccessTokenModel>

    suspend fun getTokenByRole(role: String): AccessTokenModel?

    suspend fun logout(username: String): String

    suspend fun logoutCurrentUser(): String

    suspend fun deleteAccessToken()

    suspend fun deleteAccessTokenByUsername(username: String)
}
