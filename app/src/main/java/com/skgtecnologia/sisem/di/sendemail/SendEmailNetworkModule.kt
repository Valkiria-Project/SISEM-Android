package com.skgtecnologia.sisem.di.sendemail

import com.skgtecnologia.sisem.data.sendemail.remote.SendEmailApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object SendEmailNetworkModule {

    @Provides
    internal fun provideSendEmailApi(@BearerAuthentication retrofit: Retrofit): SendEmailApi =
        retrofit.create(SendEmailApi::class.java)
}
