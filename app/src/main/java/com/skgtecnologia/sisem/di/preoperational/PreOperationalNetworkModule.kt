package com.skgtecnologia.sisem.di.preoperational

import com.skgtecnologia.sisem.data.preoperational.remote.PreOperationalApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object PreOperationalNetworkModule {

    @Provides
    internal fun providesPreOperationalApi(
        @BearerAuthentication retrofit: Retrofit
    ): PreOperationalApi = retrofit.create(PreOperationalApi::class.java)
}
