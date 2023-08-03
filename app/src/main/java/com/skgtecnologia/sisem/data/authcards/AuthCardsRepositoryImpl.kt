package com.skgtecnologia.sisem.data.authcards

import com.skgtecnologia.sisem.data.authcards.remote.AuthCardsRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import javax.inject.Inject

class AuthCardsRepositoryImpl @Inject constructor(
    private val authCardsRemoteDataSource: AuthCardsRemoteDataSource
) : AuthCardsRepository {

    override suspend fun getAuthCardsScreen(code: String, turnId: String): ScreenModel =
        authCardsRemoteDataSource.getAuthCardsScreen(code = code, turnId = turnId).getOrThrow()
}
