package com.skgtecnologia.sisem.di.incident

import com.skgtecnologia.sisem.data.incident.remote.IncidentApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object IncidentNetworkModule {

    @Provides
    internal fun providesIncidentApi(
        @BearerAuthentication retrofit: Retrofit
    ): IncidentApi = retrofit.create(IncidentApi::class.java)
}
