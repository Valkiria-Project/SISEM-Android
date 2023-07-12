package com.skgtecnologia.sisem.data.login

import com.skgtecnologia.sisem.data.login.remote.LoginScreenRemoteDataSource
import com.skgtecnologia.sisem.domain.login.LoginRepository
import com.skgtecnologia.sisem.domain.login.model.LoginScreenModel
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginScreenRemoteDatasource: LoginScreenRemoteDataSource
) : LoginRepository {

    override suspend fun getLoginScreen(serial: String): LoginScreenModel =
        loginScreenRemoteDatasource.getLoginScreen(serial).getOrThrow()
}
