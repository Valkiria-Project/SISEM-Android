package com.skgtecnologia.sisem.di.location

import com.skgtecnologia.sisem.data.location.remote.LocationApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object LocationNetworkModule {

    @Provides
    internal fun providesLocationApi(
        @BearerAuthentication retrofit: Retrofit
    ): LocationApi = retrofit.create(LocationApi::class.java)
}
