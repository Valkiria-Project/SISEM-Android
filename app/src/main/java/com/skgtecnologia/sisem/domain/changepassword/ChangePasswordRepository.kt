package com.skgtecnologia.sisem.domain.changepassword

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface ChangePasswordRepository {

    suspend fun getChangePasswordScreen(): ScreenModel

    suspend fun changePassword(oldPassword: String, newPassword: String): String
}
