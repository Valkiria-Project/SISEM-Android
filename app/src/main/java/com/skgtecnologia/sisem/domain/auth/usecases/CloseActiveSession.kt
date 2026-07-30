package com.skgtecnologia.sisem.domain.auth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import javax.inject.Inject

class CloseActiveSession @Inject constructor(
    private val authRepository: AuthRepository
) {

    @CheckResult
    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<Unit> = resultOf {
        authRepository.closeActiveSession(username, password)
    }
}
