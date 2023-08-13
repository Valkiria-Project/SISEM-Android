package com.skgtecnologia.sisem.domain.authcards

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface AuthCardsRepository {

    suspend fun getAuthCardsScreen(): ScreenModel
}
