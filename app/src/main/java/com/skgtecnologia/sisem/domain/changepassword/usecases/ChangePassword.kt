package com.skgtecnologia.sisem.domain.changepassword.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.changepassword.ChangePasswordRepository
import javax.inject.Inject

class ChangePassword @Inject constructor(
    private val changePasswordRepository: ChangePasswordRepository
) {

    @CheckResult
    suspend operator fun invoke(
        oldPassword: String,
        newPassword: String
    ): Result<String> = resultOf {
        changePasswordRepository.changePassword(
            oldPassword = oldPassword,
            newPassword = newPassword
        )
    }
}
