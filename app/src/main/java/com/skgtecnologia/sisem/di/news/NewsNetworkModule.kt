package com.skgtecnologia.sisem.di.news

import com.skgtecnologia.sisem.data.news.remote.NewsApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object NewsNetworkModule {

    @Provides
    internal fun providesNewsApi(
        @BearerAuthentication retrofit: Retrofit
    ): NewsApi = retrofit.create(NewsApi::class.java)
}
