package com.skgtecnologia.sisem.domain.operation.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import javax.inject.Inject

class GetConfig @Inject constructor(
    private val operationRepository: OperationRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ConfigModel> = resultOf {
        operationRepository.getConfig()
    }
}
