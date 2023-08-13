package com.skgtecnologia.sisem.di.operation

import com.skgtecnologia.sisem.data.operation.remote.OperationApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class]) // FIXME: should include?
@InstallIn(SingletonComponent::class)
object OperationNetworkModule {

    @Provides
    internal fun providesAuthCardsApi(@BearerAuthentication retrofit: Retrofit): OperationApi =
        retrofit.create(OperationApi::class.java)
}
