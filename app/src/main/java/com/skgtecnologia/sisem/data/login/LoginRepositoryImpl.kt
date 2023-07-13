package com.skgtecnologia.sisem.data.login

import com.skgtecnologia.sisem.data.login.remote.LoginRemoteDataSource
import com.skgtecnologia.sisem.domain.login.LoginRepository
import com.skgtecnologia.sisem.domain.login.model.LoginScreenModel
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDatasource: LoginRemoteDataSource
) : LoginRepository {

    override suspend fun getLoginScreen(serial: String): LoginScreenModel =
        loginRemoteDatasource.getLoginScreen(serial).getOrThrow()
}
