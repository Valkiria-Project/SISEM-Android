package com.skgtecnologia.sisem.data.myscreen.cache

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.myscreen.cache.model.mapToCache
import com.skgtecnologia.sisem.data.myscreen.cache.model.mapToDomain
import com.skgtecnologia.sisem.domain.myscreen.model.MyScreenModel
import javax.inject.Inject

class MyScreenCacheDataSource @Inject constructor(
    private val myScreenDao: MyScreenDao
) {

    suspend fun storeMyScreen(myScreenModel: MyScreenModel): Result<Long> = resultOf {
        myScreenDao.insertScreen(myScreenModel.mapToCache())
    }

    suspend fun getMyScreen(rowId: Long): Result<MyScreenModel> = resultOf {
        myScreenDao.getScreenByRowId(rowId)?.mapToDomain()
            ?: error("error getting MyScreen from cache")
    }
}
