package com.skgtecnologia.sisem.data.recordnews

import com.skgtecnologia.sisem.data.recordnews.remote.RecordNewsDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.recordnews.RecordNewsRepository
import javax.inject.Inject

class RecordNewsRepositoryImpl @Inject constructor(
    private val recordNewsDataSource: RecordNewsDataSource
) : RecordNewsRepository {

    override suspend fun getRecordNewsScreen(): ScreenModel =
        recordNewsDataSource.getRecordNewsScreen().getOrThrow()
}
