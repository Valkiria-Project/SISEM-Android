package com.skgtecnologia.sisem.data.loginscreen

import com.skgtecnologia.sisem.data.loginscreen.remote.LoginScreenRemoteDataSource
import com.skgtecnologia.sisem.domain.loginscreen.LoginScreenRepository
import com.skgtecnologia.sisem.domain.loginscreen.model.LoginScreenModel
import javax.inject.Inject

class LoginScreenRepositoryImpl @Inject constructor(
    private val loginScreenRemoteDatasource: LoginScreenRemoteDataSource
) : LoginScreenRepository {

    override suspend fun getLoginScreen(serial: String): LoginScreenModel =
        loginScreenRemoteDatasource.getLoginScreen(serial).getOrThrow()
}
