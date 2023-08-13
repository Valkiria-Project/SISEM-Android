package com.skgtecnologia.sisem.data.authcards

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.authcards.remote.AuthCardsRemoteDataSource
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class AuthCardsRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authCardsRemoteDataSource: AuthCardsRemoteDataSource
) : AuthCardsRepository {

    override suspend fun getAuthCardsScreen(): ScreenModel =
        authCardsRemoteDataSource.getAuthCardsScreen(
            code = "1", // FIXME: Ambulance code service
            turnId = authCacheDataSource.retrieveAccessToken()?.turn?.id?.toString().orEmpty()
        ).getOrThrow()
}
