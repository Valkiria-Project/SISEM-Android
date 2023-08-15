package com.skgtecnologia.sisem.di.changepassword

import com.skgtecnologia.sisem.data.changepassword.remote.ChangePasswordApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class]) // FIXME: should include?
@InstallIn(SingletonComponent::class)
object ChangePasswordNetworkModule {

    @Provides
    internal fun providesChangePasswordApi(@BearerAuthentication retrofit: Retrofit): ChangePasswordApi =
        retrofit.create(ChangePasswordApi::class.java)
}
