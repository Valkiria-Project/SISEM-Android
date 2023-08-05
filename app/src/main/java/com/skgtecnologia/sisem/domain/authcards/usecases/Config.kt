package com.skgtecnologia.sisem.domain.authcards.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import javax.inject.Inject

class Config @Inject constructor(
    private val authCardsRepository: AuthCardsRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ConfigModel> = resultOf {
        authCardsRepository.config()
    }
}
