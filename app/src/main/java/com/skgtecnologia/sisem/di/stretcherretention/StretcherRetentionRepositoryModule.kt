package com.skgtecnologia.sisem.di.stretcherretention

import com.skgtecnologia.sisem.data.stretcherretention.StretcherRetentionRepositoryImpl
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class StretcherRetentionRepositoryModule {

    @Binds
    abstract fun bindStretcherRetentionRepository(
        stretcherRetentionRepositoryImpl: StretcherRetentionRepositoryImpl
    ): StretcherRetentionRepository
}
