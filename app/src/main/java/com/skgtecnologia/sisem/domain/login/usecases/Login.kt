package com.skgtecnologia.sisem.domain.login.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import javax.inject.Inject

class Login @Inject constructor(
    private val authRepository: AuthRepository
) {

    @CheckResult
    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<AccessTokenModel> = resultOf {
        authRepository.authenticate(username, password)
    }
}
