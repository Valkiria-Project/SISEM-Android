package com.skgtecnologia.sisem.domain.operation.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import javax.inject.Inject

class LogoutTurn @Inject constructor(private val operationRepository: OperationRepository) {

    @CheckResult
    suspend operator fun invoke(username: String): Result<String> = resultOf {
        operationRepository.logoutTurn(username)
    }
}
