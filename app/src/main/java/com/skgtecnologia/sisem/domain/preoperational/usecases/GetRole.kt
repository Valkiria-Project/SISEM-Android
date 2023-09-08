package com.skgtecnologia.sisem.domain.preoperational.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import javax.inject.Inject

class GetRole @Inject constructor(
    private val preOperationalRepository: PreOperationalRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<OperationRole> = resultOf {
        preOperationalRepository.getRole()
    }
}
