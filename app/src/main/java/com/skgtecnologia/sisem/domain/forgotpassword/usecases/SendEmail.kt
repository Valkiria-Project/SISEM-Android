package com.skgtecnologia.sisem.domain.forgotpassword.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.forgotpassword.ForgotPasswordRepository
import javax.inject.Inject

class SendEmail @Inject constructor(
    private val forgotPasswordRepository: ForgotPasswordRepository
) {

    @CheckResult
    suspend operator fun invoke(email: String): Result<Unit> = resultOf {
        forgotPasswordRepository.sendEmail(email)
    }
}
