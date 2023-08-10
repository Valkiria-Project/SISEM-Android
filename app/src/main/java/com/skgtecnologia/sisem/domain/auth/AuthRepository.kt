package com.skgtecnologia.sisem.domain.auth

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel

interface AuthRepository {

    suspend fun authenticate(username: String, password: String): AccessTokenModel

    suspend fun getAccessToken(): String?

    suspend fun getAllAccessTokens(): List<AccessTokenModel>

    suspend fun logout(username: String): String
}
