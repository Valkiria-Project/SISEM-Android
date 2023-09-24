package com.skgtecnologia.sisem.di.clinichistory

import com.skgtecnologia.sisem.data.clinichistory.ClinicHistoryRepositoryImpl
import com.skgtecnologia.sisem.domain.clinichistory.ClinicHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ClinicHistoryRepositoryModule {

    @Binds
    abstract fun bindClinicHistoryRepository(
        clinicHistoryRepositoryImpl: ClinicHistoryRepositoryImpl
    ): ClinicHistoryRepository
}
