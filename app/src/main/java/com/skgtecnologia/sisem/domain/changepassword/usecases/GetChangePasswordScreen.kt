package com.skgtecnologia.sisem.domain.changepassword.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.changepassword.ChangePasswordRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetChangePasswordScreen @Inject constructor(
    private val changePasswordRepository: ChangePasswordRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ScreenModel> = resultOf {
        changePasswordRepository.getChangePasswordScreen()
    }
}
