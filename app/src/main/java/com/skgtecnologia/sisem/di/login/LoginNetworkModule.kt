package com.skgtecnologia.sisem.di.login

import com.skgtecnologia.sisem.data.login.remote.LoginApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object LoginNetworkModule {

    @Provides
    internal fun providesLoginApi(@BearerAuthentication retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)
}
