package com.skgtecnologia.sisem.domain.authcards.usecases

import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import javax.inject.Inject

class GetConfig @Inject constructor(
    private val authCardsRepository: AuthCardsRepository
) {
    suspend operator fun invoke() = authCardsRepository.getConfig()
}
