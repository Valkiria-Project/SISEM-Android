package com.skgtecnologia.sisem.data.changepassword.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.changepassword.remote.model.ChangePasswordBody
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class ChangePasswordRemoteDataSource @Inject constructor(
    private val changePasswordApi: ChangePasswordApi,
    private val networkApi: NetworkApi
) {

    suspend fun getChangePasswordScreen(): Result<ScreenModel> = networkApi.apiCall {
        changePasswordApi.getChangePasswordScreen(screenBody = ScreenBody(params = Params()))
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<String> = networkApi.apiCall {
        changePasswordApi.changePassword(
            ChangePasswordBody(
                oldPassword = oldPassword,
                newPassword = newPassword
            )
        )
    }.mapResult {
        it.message
    }
}
