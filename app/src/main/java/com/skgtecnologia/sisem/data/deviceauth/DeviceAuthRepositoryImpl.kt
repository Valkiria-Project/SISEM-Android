package com.skgtecnologia.sisem.data.deviceauth

import com.skgtecnologia.sisem.data.deviceauth.remote.DeviceAuthRemoteDataSource
import com.skgtecnologia.sisem.domain.deviceauth.DeviceAuthRepository
import com.skgtecnologia.sisem.domain.deviceauth.model.AssociateDeviceModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class DeviceAuthRepositoryImpl @Inject constructor(
    private val deviceAuthRemoteDataSource: DeviceAuthRemoteDataSource
) : DeviceAuthRepository {

    override suspend fun getDeviceAuthScreen(serial: String): ScreenModel =
        deviceAuthRemoteDataSource.getDeviceAuthScreen(serial).getOrThrow()

    override suspend fun associateDevice(
        licensePlate: String,
        serial: String,
        code: String
    ): AssociateDeviceModel = deviceAuthRemoteDataSource.associateDevice(licensePlate, serial, code).getOrThrow()
}
