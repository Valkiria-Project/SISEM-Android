package com.skgtecnologia.sisem.data.changepassword

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.changepassword.remote.ChangePasswordRemoteDataSource
import com.skgtecnologia.sisem.domain.changepassword.ChangePasswordRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChangePasswordRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val changePasswordRemoteDataSource: ChangePasswordRemoteDataSource
) : ChangePasswordRepository {

    override suspend fun getChangePasswordScreen(): ScreenModel =
        changePasswordRemoteDataSource.getChangePasswordScreen().getOrThrow()

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): String = changePasswordRemoteDataSource.changePassword(
        oldPassword = oldPassword,
        newPassword = newPassword
    ).onSuccess {
        authCacheDataSource.observeAccessToken().first()?.let { accessToken ->
            authCacheDataSource.storeAccessToken(accessToken.copy(isWarning = false))
        }
    }.getOrThrow()
}
