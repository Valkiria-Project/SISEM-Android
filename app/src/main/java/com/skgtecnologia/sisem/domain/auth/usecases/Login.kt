package com.skgtecnologia.sisem.domain.auth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.operation.model.PreoperationalStatus
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import javax.inject.Inject

class Login @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeOperationConfig: ObserveOperationConfig
) {

    @CheckResult
    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<AccessTokenModel> = resultOf {
        val accessToken = authRepository.authenticate(username, password)
        val operationConfig = observeOperationConfig.invoke().getOrNull()
        val configPreoperational = PreoperationalStatus.getStatusByName(
            operationConfig?.vehicleConfig?.preoperational.orEmpty()
        ) == PreoperationalStatus.SI

        val updatedAccessToken = accessToken.copy(
            configPreoperational = configPreoperational
        )

        updatedAccessToken
    }
}
