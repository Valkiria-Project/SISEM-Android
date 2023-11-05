package com.skgtecnologia.sisem.data.forgotpassword.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.banner.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class ForgotPasswordRemoteDataSource @Inject constructor(
    private val errorModelFactory: ErrorModelFactory,
    private val forgotPasswordApi: ForgotPasswordApi
) {

    suspend fun getForgotPasswordScreen(): Result<ScreenModel> = apiCall(errorModelFactory) {
        forgotPasswordApi.getForgotPasswordScreen(screenBody = ScreenBody(params = Params()))
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun sendEmail(email: String): Result<Unit> = apiCall(errorModelFactory) {
        forgotPasswordApi.sendEmail(email = email)
    }
}
