package com.skgtecnologia.sisem.di.biometric

import com.skgtecnologia.sisem.data.biometric.remote.BiometricApi
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BiometricNetworkModule {

    @Provides
    @Singleton
    fun provideBiometricApi(
        @BearerAuthentication retrofit: Retrofit
    ): BiometricApi = retrofit.create(BiometricApi::class.java)
}
