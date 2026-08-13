package com.skgtecnologia.sisem.di.biometric

import com.skgtecnologia.sisem.data.biometric.BiometricRepositoryImpl
import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BiometricRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBiometricRepository(impl: BiometricRepositoryImpl): BiometricRepository
}
