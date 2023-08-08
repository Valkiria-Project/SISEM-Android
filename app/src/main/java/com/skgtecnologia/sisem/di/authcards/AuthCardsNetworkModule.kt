package com.skgtecnologia.sisem.di.authcards

import com.skgtecnologia.sisem.data.authcards.remote.AuthCardsApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class]) // FIXME: should include?
@InstallIn(SingletonComponent::class)
object AuthCardsNetworkModule {

    @Provides
    internal fun providesAuthCardsApi(@BearerAuthentication retrofit: Retrofit): AuthCardsApi =
        retrofit.create(AuthCardsApi::class.java)
}
