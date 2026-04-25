package com.skgtecnologia.sisem.domain.forgotpassword

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface ForgotPasswordRepository {

    suspend fun getForgotPasswordScreen(): ScreenModel

    suspend fun sendEmail(email: String)
}
