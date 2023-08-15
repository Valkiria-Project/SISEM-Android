package com.skgtecnologia.sisem.domain.operation.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import javax.inject.Inject

class GetOperationConfig @Inject constructor(
    private val operationRepository: OperationRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<OperationModel> = resultOf {
        operationRepository.getOperationConfig(serial)
    }
}
