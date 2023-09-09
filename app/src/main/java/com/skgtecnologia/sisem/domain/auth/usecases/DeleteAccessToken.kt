package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import javax.inject.Inject

class DeleteAccessToken @Inject constructor(
    private val authRepository: AuthRepository,
    private val logout: Logout
) {

    suspend operator fun invoke(): Result<Unit> = resultOf {
        // FIXME: Validate this
        authRepository.getAllAccessTokens().forEach { accessToken ->
            logout.invoke(accessToken.username)
                .onSuccess {
                    authRepository.deleteAccessTokenByUsername(accessToken.username)
                }.getOrThrow()
        }
    }
}
