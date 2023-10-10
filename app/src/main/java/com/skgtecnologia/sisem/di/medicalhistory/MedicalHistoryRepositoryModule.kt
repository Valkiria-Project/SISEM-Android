package com.skgtecnologia.sisem.di.medicalhistory

import com.skgtecnologia.sisem.data.medicalhistory.MedicalHistoryRepositoryImpl
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MedicalHistoryRepositoryModule {

    @Binds
    abstract fun bindMedicalHistoryRepository(
        medicalHistoryRepositoryImpl: MedicalHistoryRepositoryImpl
    ): MedicalHistoryRepository
}
