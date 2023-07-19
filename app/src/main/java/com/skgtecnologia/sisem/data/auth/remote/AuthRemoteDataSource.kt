package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.mapToDomain
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun authenticate(username: String, password: String): Result<AccessTokenModel> =
        resultOf {
            val response = withContext(Dispatchers.IO) {
                authApi.authenticate(
                    authenticateBody = AuthenticateBody(
                        username = username,
                        password = password
                    )
                )
            }

            val body = response.body()

            if (response.isSuccessful && body != null) {
                body.mapToDomain()
            } else {
                Timber.d("The retrieved response is not successful and/or body is empty")
                error("The retrieved response is not successful and/or body is empty")
            }
        }
}
