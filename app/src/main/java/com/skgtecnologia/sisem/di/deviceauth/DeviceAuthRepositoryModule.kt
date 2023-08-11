package com.skgtecnologia.sisem.di.deviceauth

import com.skgtecnologia.sisem.data.deviceauth.DeviceAuthRepositoryImpl
import com.skgtecnologia.sisem.domain.deviceauth.DeviceAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DeviceAuthRepositoryModule {

    @Binds
    abstract fun bindDeviceAuthRepository(
        deviceAuthRepositoryImpl: DeviceAuthRepositoryImpl
    ): DeviceAuthRepository
}
