package com.skgtecnologia.sisem.domain.operation.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import javax.inject.Inject

class StoreAmbulanceCode @Inject constructor(
    private val operationRepository: OperationRepository
) {

    suspend operator fun invoke(code: String): Result<Unit> = resultOf {
        operationRepository.storeAmbulanceCode(code)
    }
}
