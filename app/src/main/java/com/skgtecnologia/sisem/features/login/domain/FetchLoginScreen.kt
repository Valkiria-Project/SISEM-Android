package com.skgtecnologia.sisem.features.login.domain

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.login.LoginRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import timber.log.Timber
import javax.inject.Inject

class FetchLoginScreen @Inject constructor(
    private val loginRepository: LoginRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<ScreenModel> = resultOf {
        Timber.d("Serial: $serial")
        loginRepository.getLoginScreen(serial)
    }
}
