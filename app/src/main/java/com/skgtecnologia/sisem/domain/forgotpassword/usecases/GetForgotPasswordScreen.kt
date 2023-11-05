package com.skgtecnologia.sisem.domain.forgotpassword.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.forgotpassword.ForgotPasswordRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetForgotPasswordScreen @Inject constructor(
    private val forgotPasswordRepository: ForgotPasswordRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ScreenModel> = resultOf {
        forgotPasswordRepository.getForgotPasswordScreen()
    }
}
