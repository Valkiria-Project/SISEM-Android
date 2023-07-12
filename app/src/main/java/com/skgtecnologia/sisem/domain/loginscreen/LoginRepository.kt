package com.skgtecnologia.sisem.domain.loginscreen

import com.skgtecnologia.sisem.domain.loginscreen.model.LoginScreenModel

interface LoginRepository {

    suspend fun getLoginScreen(serial: String): LoginScreenModel
}
