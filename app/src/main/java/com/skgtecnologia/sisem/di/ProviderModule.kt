package com.skgtecnologia.sisem.di

import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.commons.resources.AndroidIdProviderImpl
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.commons.resources.StringProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProviderModule {

    @Binds
    internal abstract fun bindsAndroidIdProvider(
        androidIdProviderImpl: AndroidIdProviderImpl
    ): AndroidIdProvider

    @Binds
    internal abstract fun bindsStringProvider(
        stringProviderImpl: StringProviderImpl
    ): StringProvider
}
