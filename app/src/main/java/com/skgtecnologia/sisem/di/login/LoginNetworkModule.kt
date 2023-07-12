package com.skgtecnologia.sisem.di.login

import com.skgtecnologia.sisem.data.login.remote.api.LoginApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object LoginNetworkModule {

    @Provides
    internal fun providesLoginApi(retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)
}
