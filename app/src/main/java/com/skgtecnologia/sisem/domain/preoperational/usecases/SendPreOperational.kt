package com.skgtecnologia.sisem.domain.preoperational.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import javax.inject.Inject

class SendPreOperational @Inject constructor(
    private val preOperationalRepository: PreOperationalRepository
) {

    @CheckResult
    suspend operator fun invoke(extraData: Map<String, String>): Result<Unit> = resultOf {
        preOperationalRepository.sendPreOperational(extraData)
    }
}
