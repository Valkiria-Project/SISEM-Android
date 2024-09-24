package com.skgtecnologia.sisem.domain.operation.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class ObserveOperationConfig @Inject constructor(
    private val operationRepository: OperationRepository
) {

    @CheckResult
    operator fun invoke(): Flow<OperationModel> =
        operationRepository.observeOperationConfig().filterNotNull()
}
