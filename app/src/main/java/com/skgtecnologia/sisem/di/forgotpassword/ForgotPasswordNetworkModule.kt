package com.skgtecnologia.sisem.di.forgotpassword

import com.skgtecnologia.sisem.data.forgotpassword.remote.ForgotPasswordApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object ForgotPasswordNetworkModule {

    @Provides
    internal fun providesForgotPasswordApi(
        @BearerAuthentication retrofit: Retrofit
    ): ForgotPasswordApi = retrofit.create(ForgotPasswordApi::class.java)
}
