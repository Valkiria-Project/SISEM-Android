package com.skgtecnologia.sisem.di.medicalhistory

import com.skgtecnologia.sisem.data.medicalhistory.remote.MedicalHistoryApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object MedicalHistoryNetworkModule {

    @Provides
    internal fun providesMedicalHistoryApi(
        @BearerAuthentication retrofit: Retrofit
    ): MedicalHistoryApi = retrofit.create(MedicalHistoryApi::class.java)
}
