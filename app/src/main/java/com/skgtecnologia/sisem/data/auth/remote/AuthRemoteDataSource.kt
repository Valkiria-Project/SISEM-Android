package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun authenticate(username: String, password: String): Result<AccessTokenModel> =
        apiCall {
            authApi.authenticate(
                authenticateBody = AuthenticateBody(
                    username = username,
                    password = password,
                    code = "1", // FIXME: Hardcoded data
                    idTurn = null, // FIXME: Hardcoded data
                )
            )
        }.mapResult {
            it.mapToDomain()
        }
}
