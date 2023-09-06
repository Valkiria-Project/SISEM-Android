package com.skgtecnologia.sisem.domain.recordnews.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.recordnews.RecordNewsRepository
import javax.inject.Inject

class GetRecordNewsScreen @Inject constructor(
    private val recordNewsRepository: RecordNewsRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ScreenModel> = resultOf {
        recordNewsRepository.getRecordNewsScreen()
    }
}
