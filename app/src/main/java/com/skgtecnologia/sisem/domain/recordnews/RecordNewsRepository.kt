package com.skgtecnologia.sisem.domain.recordnews

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface RecordNewsRepository {

    suspend fun getRecordNewsScreen(): ScreenModel
}
