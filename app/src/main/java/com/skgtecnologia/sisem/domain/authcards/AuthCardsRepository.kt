package com.skgtecnologia.sisem.domain.authcards

import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface AuthCardsRepository {

    suspend fun config(): ConfigModel

    suspend fun getConfig(): ConfigModel?

    suspend fun getAuthCardsScreen(code: String, turnId: String): ScreenModel
}
