package com.skgtecnologia.sisem.domain.changepassword.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import javax.inject.Inject

class OnCancel @Inject constructor(
    private val authRepository: AuthRepository,
    private val logout: Logout
) {

    @CheckResult
    suspend operator fun invoke(): Result<Unit> = resultOf {
        val lastUserName = authRepository.getLastAccessToken()?.username.orEmpty()
        logout.invoke(lastUserName)
            .onSuccess {
                authRepository.deleteAccessTokenByUsername(lastUserName)
            }.getOrThrow()
    }
}
