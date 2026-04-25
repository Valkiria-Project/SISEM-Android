package com.skgtecnologia.sisem.di.stretcherretention

import com.skgtecnologia.sisem.data.stretcherretention.remote.StretcherRetentionApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object StretcherRetentionNetworkModule {

    @Provides
    internal fun providesStretcherRetentionApi(
        @BearerAuthentication retrofit: Retrofit
    ): StretcherRetentionApi = retrofit.create(StretcherRetentionApi::class.java)
}
