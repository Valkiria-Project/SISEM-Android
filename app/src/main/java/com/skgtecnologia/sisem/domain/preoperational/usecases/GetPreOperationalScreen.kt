package com.skgtecnologia.sisem.domain.preoperational.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import javax.inject.Inject

class GetPreOperationalScreen @Inject constructor(
    private val preOperationalRepository: PreOperationalRepository
) {

    @CheckResult
    suspend operator fun invoke(roleName: String?, androidId: String): Result<ScreenModel> =
        resultOf {
            preOperationalRepository.getPreOperationalScreen(roleName, androidId)
        }
}
