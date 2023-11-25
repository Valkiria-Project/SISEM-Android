package com.skgtecnologia.sisem.domain.signature.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.signature.SignatureRepository
import javax.inject.Inject

class GetInitSignatureScreen @Inject constructor(
    private val signatureRepository: SignatureRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ScreenModel> = resultOf {
        signatureRepository.getInitSignatureScreen()
    }
}
