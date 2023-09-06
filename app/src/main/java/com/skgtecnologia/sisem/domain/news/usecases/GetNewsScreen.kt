package com.skgtecnologia.sisem.domain.news.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.news.NewsRepository
import javax.inject.Inject

class GetNewsScreen @Inject constructor(
    private val newsRepository: NewsRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<ScreenModel> = resultOf {
        newsRepository.getAddReportScreen(serial)
    }
}
