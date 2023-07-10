package com.skgtecnologia.sisem.data.loginscreen.remote

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.loginscreen.remote.model.LoginScreenBody
import com.skgtecnologia.sisem.data.loginscreen.remote.model.Params
import com.skgtecnologia.sisem.data.loginscreen.remote.model.mapToDomain
import com.skgtecnologia.sisem.domain.loginscreen.model.LoginScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import timber.log.Timber

class LoginScreenRemoteDataSource @Inject constructor(
    private val loginScreenApi: LoginScreenApi
) {

    suspend fun getLoginScreen(serial: String): Result<LoginScreenModel> = resultOf {
        val response = withContext(Dispatchers.IO) {
            loginScreenApi.getLoginScreen(
                loginScreenBody = LoginScreenBody(
                    params = Params(
                        serial = serial
                    )
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
