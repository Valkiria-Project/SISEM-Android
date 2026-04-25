package com.skgtecnologia.sisem.di.signature

import com.skgtecnologia.sisem.data.signature.remote.SignatureApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object SignatureNetworkModule {

    @Provides
    internal fun provideSignatureApi(@BearerAuthentication retrofit: Retrofit): SignatureApi =
        retrofit.create(SignatureApi::class.java)
}
