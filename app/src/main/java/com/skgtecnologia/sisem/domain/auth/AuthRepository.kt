package com.skgtecnologia.sisem.domain.auth

import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel

interface AuthRepository {

    suspend fun authenticate(username: String, password: String): AccessTokenModel

    suspend fun getAccessToken(): String?
}