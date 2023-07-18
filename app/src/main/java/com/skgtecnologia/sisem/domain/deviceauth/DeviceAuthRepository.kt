package com.skgtecnologia.sisem.domain.deviceauth

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface DeviceAuthRepository {

    suspend fun getDeviceAuthScreen(serial: String): ScreenModel

    suspend fun associateDevice(licensePlate: String, serial: String, code: String): String
}
