package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.RefreshBody
import com.skgtecnologia.sisem.data.auth.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.model.RefreshTokenModel
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun authenticate(
        username: String,
        password: String,
        code: String,
        turnId: String
    ): Result<AccessTokenModel> = apiCall {
        authApi.authenticate(
            authenticateBody = AuthenticateBody(
                username = username,
                password = password,
                code = code,
                idTurn = turnId
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun refreshToken(refreshToken: String): Result<RefreshTokenModel> = apiCall {
        authApi.refresh(refreshBody = RefreshBody(refreshToken = refreshToken))
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun logout(username: String): Result<String> = apiCall {
        authApi.logout(username = username)
    }.mapResult {
        username
    }
}
