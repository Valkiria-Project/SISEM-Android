package com.skgtecnologia.sisem.di.inventory

import com.skgtecnologia.sisem.data.inventory.remote.InventoryApi
import com.skgtecnologia.sisem.di.BearerNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [BearerNetworkModule::class])
@InstallIn(SingletonComponent::class)
object InventoryNetworkModule {

    @Provides
    internal fun providesInventoryApi(
        @BearerAuthentication retrofit: Retrofit
    ): InventoryApi = retrofit.create(InventoryApi::class.java)
}
