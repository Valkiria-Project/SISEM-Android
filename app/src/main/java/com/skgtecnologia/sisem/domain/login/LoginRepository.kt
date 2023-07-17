package com.skgtecnologia.sisem.domain.login

import com.skgtecnologia.sisem.domain.login.model.LoginModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface LoginRepository {

    suspend fun getLoginScreen(serial: String): ScreenModel

    suspend fun login(username: String, password: String): LoginModel
}
