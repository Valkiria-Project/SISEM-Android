package com.skgtecnologia.sisem.domain.login

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface LoginRepository {

    suspend fun getLoginScreen(serial: String): ScreenModel
}
