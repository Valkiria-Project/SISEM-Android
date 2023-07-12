package com.skgtecnologia.sisem.domain.login

import com.skgtecnologia.sisem.domain.login.model.LoginScreenModel

interface LoginRepository {

    suspend fun getLoginScreen(serial: String): LoginScreenModel
}
