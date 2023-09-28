package com.skgtecnologia.sisem.domain.operation.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ObserveOperationConfig @Inject constructor(
    private val authRepository: AuthRepository,
    private val operationRepository: OperationRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<OperationModel> = resultOf {
        combine(
            operationRepository.observeOperationConfig(),
            authRepository.observeCurrentAccessToken()
        ) { operationModel, accessTokenModel ->
            val operationConfigModel = operationModel?.copy(
                operationRole = OperationRole.getRoleByName(accessTokenModel?.role.orEmpty())
            )

            checkNotNull(operationConfigModel)
        }.first()
    }
}
