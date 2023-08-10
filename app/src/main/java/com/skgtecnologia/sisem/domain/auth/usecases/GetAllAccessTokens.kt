package com.skgtecnologia.sisem.domain.auth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import javax.inject.Inject

class GetAllAccessTokens @Inject constructor(
    private val authRepository: AuthRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<List<AccessTokenModel>> = resultOf {
        authRepository.getAllAccessTokens()
    }
}
