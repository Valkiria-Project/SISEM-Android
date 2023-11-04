package com.skgtecnologia.sisem.data.login.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(
    private val loginApi: LoginApi
) {

    suspend fun getLoginScreen(serial: String): Result<ScreenModel> = apiCall {
        loginApi.getLoginScreen(screenBody = ScreenBody(params = Params(serial = serial)))
    }.mapResult {
        it.mapToDomain()
    }
}
