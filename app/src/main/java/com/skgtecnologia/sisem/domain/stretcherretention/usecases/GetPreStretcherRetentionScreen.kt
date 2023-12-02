package com.skgtecnologia.sisem.domain.stretcherretention.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import javax.inject.Inject

class GetPreStretcherRetentionScreen @Inject constructor(
    private val stretcherRetentionRepository: StretcherRetentionRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ScreenModel> = resultOf {
        stretcherRetentionRepository.getPreStretcherRetentionScreen()
    }
}
