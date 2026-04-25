package com.skgtecnologia.sisem.domain.signature.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.signature.SignatureRepository
import javax.inject.Inject

class SearchDocument @Inject constructor(
    private val signatureRepository: SignatureRepository
) {

    @CheckResult
    suspend operator fun invoke(document: String): Result<Unit> = resultOf {
        signatureRepository.searchDocument(document)
    }
}
