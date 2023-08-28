package com.skgtecnologia.sisem.domain.news

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface NewsRepository {

    suspend fun getNewsScreen(serial: String): ScreenModel
}
