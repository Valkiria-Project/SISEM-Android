package com.skgtecnologia.sisem.domain.auth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import javax.inject.Inject

class LogoutCurrentUser @Inject constructor(private val authRepository: AuthRepository) {

    @CheckResult
    suspend operator fun invoke(): Result<String> = resultOf {
        authRepository.logoutCurrentUser()
    }
}
