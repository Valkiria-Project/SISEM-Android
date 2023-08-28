package com.skgtecnologia.sisem.data.news

import com.skgtecnologia.sisem.data.news.remote.NewsRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.news.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
) : NewsRepository {

    override suspend fun getNewsScreen(serial: String): ScreenModel =
        newsRemoteDataSource.getNewsScreen(serial).getOrThrow()
}
