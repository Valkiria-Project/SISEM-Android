package com.skgtecnologia.sisem.data.login

import com.skgtecnologia.sisem.data.login.cache.LoginCacheDataSource
import com.skgtecnologia.sisem.data.login.remote.LoginRemoteDataSource
import com.skgtecnologia.sisem.domain.login.LoginRepository
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginCacheDataSource: LoginCacheDataSource,
    private val loginRemoteDatasource: LoginRemoteDataSource
) : LoginRepository {

    override suspend fun getLoginScreen(serial: String): ScreenModel =
        loginRemoteDatasource.getLoginScreen(serial).getOrThrow()

    override suspend fun login(username: String, password: String): AccessTokenModel =
        loginRemoteDatasource.login(username, password)
            .onSuccess { accessTokenModel ->
                loginCacheDataSource.storeAccessToken(accessTokenModel)
            }
            .getOrThrow()
}
