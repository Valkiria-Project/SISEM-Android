package com.skgtecnologia.sisem.domain.stretcherretention.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import javax.inject.Inject

class SaveStretcherRetention @Inject constructor(
    private val stretcherRetentionRepository: StretcherRetentionRepository
) {

    @CheckResult
    suspend operator fun invoke(
        idAph: String,
        fieldsValue: Map<String, String>,
        chipSelectionValues: Map<String, String>
    ): Result<Unit> = resultOf {
        stretcherRetentionRepository.saveStretcherRetention(
            idAph = idAph,
            fieldsValue = fieldsValue,
            chipSelectionValues = chipSelectionValues
        )
    }
}
