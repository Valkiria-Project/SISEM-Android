package com.skgtecnologia.sisem.domain.deviceauth

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface DeviceAuthRepository {

    suspend fun getDeviceAuthScreen(serial: String): ScreenModel
}
