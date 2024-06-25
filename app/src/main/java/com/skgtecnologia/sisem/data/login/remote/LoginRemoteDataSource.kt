package com.skgtecnologia.sisem.data.login.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor(
    private val loginApi: LoginApi,
    private val networkApi: NetworkApi
) {

    suspend fun getLoginScreen(serial: String): Result<ScreenModel> = networkApi.apiCall {
        loginApi.getLoginScreen(screenBody = ScreenBody(params = Params(serial = serial)))
    }.mapResult {
        it.mapToDomain()
    }
}
