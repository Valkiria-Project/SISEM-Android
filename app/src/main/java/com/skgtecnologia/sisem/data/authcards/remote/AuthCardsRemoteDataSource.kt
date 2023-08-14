package com.skgtecnologia.sisem.data.authcards.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class AuthCardsRemoteDataSource @Inject constructor(
    private val authCardsApi: AuthCardsApi
) {

    suspend fun getAuthCardsScreen(code: String, turnId: String): Result<ScreenModel> = apiCall {
        authCardsApi.getAuthCardsScreen(
            screenBody = ScreenBody(
                params = Params(
                    code = code,
                    turnId = turnId
                )
            )
        )
    }.mapResult {
        it.mapToDomain()
    }
}
