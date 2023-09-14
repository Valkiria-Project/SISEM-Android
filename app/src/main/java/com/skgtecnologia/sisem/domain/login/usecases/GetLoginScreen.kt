package com.skgtecnologia.sisem.domain.login.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.login.LoginRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetLoginScreen @Inject constructor(
    private val loginRepository: LoginRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<ScreenModel> = resultOf {
        loginRepository.getLoginScreen(serial)
    }
}
