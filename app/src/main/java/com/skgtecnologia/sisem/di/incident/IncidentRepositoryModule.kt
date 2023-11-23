package com.skgtecnologia.sisem.di.incident

import com.skgtecnologia.sisem.data.incident.IncidentRepositoryImpl
import com.skgtecnologia.sisem.domain.incident.IncidentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class IncidentRepositoryModule {

    @Binds
    abstract fun bindIncidentRepository(
        incidentRepositoryImpl: IncidentRepositoryImpl
    ): IncidentRepository
}
