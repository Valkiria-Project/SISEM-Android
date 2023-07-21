package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.commons.extensions.recoverResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.handleThrowable
import com.skgtecnologia.sisem.data.remote.extensions.toError
import com.skgtecnologia.sisem.data.remote.model.error.mapToDomain
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun authenticate(username: String, password: String): Result<AccessTokenModel> =
//        apiCall {
//            authApi.authenticate(
//                authenticateBody = AuthenticateBody(
//                    username = username,
//                    password = password
//                )
//            )
//        }.mapResult {
//            it.mapToDomain()
//        }
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
                Timber.wtf("The retrieved response is not successful and/or body is empty")
                val errorType = response.errorBody().toError()
                errorType?.mapToDomain()?.let {
                    throw it
                } ?: error("The retrieved response is not successful and/or body is empty")
            }
        }.recoverResult {
            throw it.handleThrowable()
        }
}
