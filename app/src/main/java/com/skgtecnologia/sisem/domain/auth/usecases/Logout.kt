package com.skgtecnologia.sisem.domain.auth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import javax.inject.Inject

class Logout @Inject constructor(private val authRepository: AuthRepository) {

    @CheckResult
    suspend operator fun invoke(username: String): Result<String> = resultOf {
        authRepository.logout(username)
    }
}
