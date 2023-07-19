package com.skgtecnologia.sisem.data.login.remote

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginRemoteDataSource @Inject constructor(
    private val loginApi: LoginApi
) {

    suspend fun getLoginScreen(serial: String): Result<ScreenModel> = resultOf {
        val response = withContext(Dispatchers.IO) {
            loginApi.getLoginScreen(screenBody = ScreenBody(params = Params(serial = serial)))
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
