package com.skgtecnologia.sisem.data.news.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(
    private val newsApi: NewsApi
) {

    suspend fun getNewsScreen(serial: String): Result<ScreenModel> = apiCall {
        newsApi.getNewsScreen(screenBody = ScreenBody(params = Params(serial = serial)))
    }.mapResult {
        it.mapToDomain()
    }
}
