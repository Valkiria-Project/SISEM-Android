package com.skgtecnologia.sisem.data.authcards

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.authcards.remote.AuthCardsRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.authcards.AuthCardsRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class AuthCardsRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authCardsRemoteDataSource: AuthCardsRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : AuthCardsRepository {

    override suspend fun getAuthCardsScreen(): ScreenModel =
        authCardsRemoteDataSource.getAuthCardsScreen(
            code = operationCacheDataSource.retrieveConfig()?.ambulanceCode.orEmpty(),
            turnId = authCacheDataSource.retrieveAccessToken()?.turn?.id?.toString().orEmpty()
        ).getOrThrow()
}
