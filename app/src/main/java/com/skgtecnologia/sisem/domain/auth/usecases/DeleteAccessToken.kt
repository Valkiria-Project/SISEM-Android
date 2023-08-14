package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import javax.inject.Inject

class DeleteAccessToken @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<Unit> = resultOf {
        authRepository.deleteAccessToken()
    }
}
