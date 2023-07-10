package com.skgtecnologia.sisem.di.loginscreen

import com.skgtecnologia.sisem.data.loginscreen.remote.LoginScreenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object LoginScreenNetworkModule {

    @Provides
    internal fun providesLoginScreenApi(retrofit: Retrofit): LoginScreenApi =
        retrofit.create(LoginScreenApi::class.java)
}
