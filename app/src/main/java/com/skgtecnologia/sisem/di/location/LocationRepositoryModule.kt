package com.skgtecnologia.sisem.di.location

import com.skgtecnologia.sisem.data.location.LocationRepositoryImpl
import com.skgtecnologia.sisem.domain.location.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationRepositoryModule {

    @Binds
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository
}
