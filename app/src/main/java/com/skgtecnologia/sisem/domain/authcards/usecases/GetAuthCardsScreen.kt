package com.skgtecnologia.sisem.domain.authcards.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetAuthCardsScreen @Inject constructor(
    private val authCardsRepository: AuthCardsRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<ScreenModel> = resultOf {
        authCardsRepository.getAuthCardsScreen(serial)
    }
}
