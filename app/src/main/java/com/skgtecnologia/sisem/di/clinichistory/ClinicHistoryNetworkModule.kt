package com.skgtecnologia.sisem.di.clinichistory

import com.skgtecnologia.sisem.data.clinichistory.remote.ClinicHistoryApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object ClinicHistoryNetworkModule {

    @Provides
    internal fun providesClinicHistoryApi(
        @BearerAuthentication retrofit: Retrofit
    ): ClinicHistoryApi = retrofit.create(ClinicHistoryApi::class.java)
}
