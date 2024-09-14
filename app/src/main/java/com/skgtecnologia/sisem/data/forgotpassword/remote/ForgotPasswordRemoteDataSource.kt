package com.skgtecnologia.sisem.data.forgotpassword.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.core.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.core.data.remote.model.screen.Params
import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class ForgotPasswordRemoteDataSource @Inject constructor(
    private val forgotPasswordApi: ForgotPasswordApi,
    private val networkApi: NetworkApi
) {

    suspend fun getForgotPasswordScreen(): Result<ScreenModel> = networkApi.apiCall {
        forgotPasswordApi.getForgotPasswordScreen(screenBody = ScreenBody(params = Params()))
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun sendEmail(email: String): Result<Unit> = networkApi.apiCall {
        forgotPasswordApi.sendEmail(email = email)
    }
}
