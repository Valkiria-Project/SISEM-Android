package com.skgtecnologia.sisem.di.login

import com.skgtecnologia.sisem.data.login.remote.LoginApi
import com.skgtecnologia.sisem.di.auth.AuthNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BasicAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [AuthNetworkModule::class])
@InstallIn(SingletonComponent::class)
object LoginNetworkModule {

    @Provides
    internal fun providesLoginApi(@BasicAuthentication retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)
}
