package com.skgtecnologia.sisem.di.deviceauth

import com.skgtecnologia.sisem.data.deviceauth.remote.DeviceAuthApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object DeviceAuthNetworkModule {

    @Provides
    internal fun providesDeviceAuthApi(@BearerAuthentication retrofit: Retrofit): DeviceAuthApi =
        retrofit.create(DeviceAuthApi::class.java)
}
