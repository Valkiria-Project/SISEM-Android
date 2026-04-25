package com.skgtecnologia.sisem.data.forgotpassword

import com.skgtecnologia.sisem.data.forgotpassword.remote.ForgotPasswordRemoteDataSource
import com.skgtecnologia.sisem.domain.forgotpassword.ForgotPasswordRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class ForgotPasswordRepositoryImpl @Inject constructor(
    private val forgotPasswordRemoteDataSource: ForgotPasswordRemoteDataSource
) : ForgotPasswordRepository {

    override suspend fun getForgotPasswordScreen(): ScreenModel =
        forgotPasswordRemoteDataSource.getForgotPasswordScreen().getOrThrow()

    override suspend fun sendEmail(email: String) =
        forgotPasswordRemoteDataSource.sendEmail(email).getOrThrow()
}
