package com.skgtecnologia.sisem.data.authcards

import com.skgtecnologia.sisem.data.authcards.cache.AuthCardsCacheDataSource
import com.skgtecnologia.sisem.data.authcards.remote.AuthCardsRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import javax.inject.Inject

class AuthCardsRepositoryImpl @Inject constructor(
    private val authCardsCacheDataSource: AuthCardsCacheDataSource,
    private val authCardsRemoteDataSource: AuthCardsRemoteDataSource
) : AuthCardsRepository {
    override suspend fun config(): ConfigModel =
        authCardsRemoteDataSource.getConfig()
            .onSuccess {
                authCardsCacheDataSource.storeConfig(it)
            }.getOrThrow()

    override suspend fun getConfig(): ConfigModel? = authCardsCacheDataSource.retrieveConfig()

    override suspend fun getAuthCardsScreen(code: String, turnId: String): ScreenModel =
        authCardsRemoteDataSource.getAuthCardsScreen(code = code, turnId = turnId).getOrThrow()
}
