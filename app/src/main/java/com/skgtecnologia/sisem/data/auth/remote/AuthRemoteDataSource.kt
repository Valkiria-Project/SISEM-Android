package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.model.error.ErrorModelFactory
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val errorModelFactory: ErrorModelFactory
) {

    suspend fun authenticate(
        username: String,
        password: String,
        code: String,
        turnId: String
    ): Result<AccessTokenModel> =
        apiCall(errorModelFactory) {
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

    suspend fun logout(username: String): Result<String> = apiCall(errorModelFactory) {
        authApi.logout(username = username)
    }.mapResult {
        it.message
    }
}
