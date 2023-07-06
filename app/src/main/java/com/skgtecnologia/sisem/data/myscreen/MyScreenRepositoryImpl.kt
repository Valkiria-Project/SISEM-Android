package com.skgtecnologia.sisem.data.myscreen

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.myscreen.cache.MyScreenCacheDataSource
import com.skgtecnologia.sisem.data.myscreen.remote.MyScreenRemoteDataSource
import com.skgtecnologia.sisem.domain.myscreen.MyScreenRepository
import com.skgtecnologia.sisem.domain.myscreen.model.MyScreenModel
import timber.log.Timber
import javax.inject.Inject

class MyScreenRepositoryImpl @Inject constructor(
    private val myScreenCacheDataSource: MyScreenCacheDataSource,
    private val myScreenRemoteDataSource: MyScreenRemoteDataSource
) : MyScreenRepository {

    override suspend fun fetchMyScreen(): Long =
        myScreenRemoteDataSource
            .getMyScreen()
            .mapResult { myScreenModel ->
                Timber.d("Persist the result from remote in cache")

                myScreenCacheDataSource
                    .storeMyScreen(myScreenModel)
                    .getOrThrow()
            }
            .getOrThrow()

    override suspend fun getMyScreen(myScreenIdentifier: Long): MyScreenModel =
        myScreenCacheDataSource.getMyScreen(myScreenIdentifier).getOrThrow()
}
