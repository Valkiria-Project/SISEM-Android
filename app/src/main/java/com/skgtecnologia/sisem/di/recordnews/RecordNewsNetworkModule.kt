package com.skgtecnologia.sisem.di.recordnews

import com.skgtecnologia.sisem.data.recordnews.remote.RecordNewsApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object RecordNewsNetworkModule {

    @Provides
    internal fun providesRecordNewsApi(
        @BearerAuthentication retrofit: Retrofit
    ): RecordNewsApi = retrofit.create(RecordNewsApi::class.java)
}
