package com.skgtecnologia.sisem.di.recordnews

import com.skgtecnologia.sisem.data.recordnews.RecordNewsRepositoryImpl
import com.skgtecnologia.sisem.domain.recordnews.RecordNewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RecordNewsRepositoryModule {

    @Binds
    abstract fun bindRecordNewsRepository(
        recordNewsRepositoryImpl: RecordNewsRepositoryImpl
    ): RecordNewsRepository
}
