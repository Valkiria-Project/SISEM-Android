package com.skgtecnologia.sisem.data.login.remote

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.login.remote.api.LoginApi
import com.skgtecnologia.sisem.data.login.remote.model.LoginScreenBody
import com.skgtecnologia.sisem.data.login.remote.model.Params
import com.skgtecnologia.sisem.data.login.remote.model.mapToDomain
import com.skgtecnologia.sisem.domain.login.model.LoginScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(
    private val loginApi: LoginApi
) {

    suspend fun getLoginScreen(serial: String): Result<LoginScreenModel> = resultOf {
        val response = withContext(Dispatchers.IO) {
            loginApi.getLoginScreen(
                loginScreenBody = LoginScreenBody(params = Params(serial = serial))
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
