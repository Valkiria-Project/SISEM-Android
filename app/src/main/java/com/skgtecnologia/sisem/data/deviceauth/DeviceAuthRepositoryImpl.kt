package com.skgtecnologia.sisem.data.deviceauth

import com.skgtecnologia.sisem.data.deviceauth.remote.DeviceAuthRemoteDataSource
import com.skgtecnologia.sisem.domain.deviceauth.DeviceAuthRepository
import com.skgtecnologia.sisem.domain.deviceauth.model.AssociateDeviceModel
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class DeviceAuthRepositoryImpl @Inject constructor(
    private val deviceAuthRemoteDataSource: DeviceAuthRemoteDataSource
) : DeviceAuthRepository {

    override suspend fun getDeviceAuthScreen(serial: String): ScreenModel =
        deviceAuthRemoteDataSource.getDeviceAuthScreen(serial).getOrThrow()

    override suspend fun associateDevice(
        serial: String,
        code: String,
        disassociateDevice: Boolean
    ): AssociateDeviceModel = deviceAuthRemoteDataSource.associateDevice(
        serial, code, disassociateDevice
    ).getOrThrow()

    override suspend fun getDeviceType(code: String): DeviceModel =
        deviceAuthRemoteDataSource.getDeviceType(code).getOrThrow()
}
