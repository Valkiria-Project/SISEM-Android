package com.skgtecnologia.sisem.di.preoperational

import com.skgtecnologia.sisem.data.preoperational.PreOperationalRepositoryImpl
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PreOperationalRepositoryModule {

    @Binds
    abstract fun bindPreOperationalRepository(
        preOperationalRepositoryImpl: PreOperationalRepositoryImpl
    ): PreOperationalRepository
}
