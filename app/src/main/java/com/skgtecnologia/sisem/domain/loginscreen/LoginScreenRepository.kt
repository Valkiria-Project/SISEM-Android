package com.skgtecnologia.sisem.domain.loginscreen

import com.skgtecnologia.sisem.domain.loginscreen.model.LoginScreenModel

interface LoginScreenRepository {

    suspend fun getLoginScreen(serial: String): LoginScreenModel
}