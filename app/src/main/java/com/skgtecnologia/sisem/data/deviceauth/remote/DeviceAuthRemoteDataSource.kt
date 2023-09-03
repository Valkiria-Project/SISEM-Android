package com.skgtecnologia.sisem.data.deviceauth.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.data.deviceauth.remote.model.AssociateDeviceBody
import com.skgtecnologia.sisem.data.deviceauth.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.deviceauth.model.AssociateDeviceModel
import com.skgtecnologia.sisem.domain.model.error.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class DeviceAuthRemoteDataSource @Inject constructor(
    private val deviceAuthApi: DeviceAuthApi,
    private val errorModelFactory: ErrorModelFactory
) {

    suspend fun getDeviceAuthScreen(serial: String): Result<ScreenModel> = apiCall(errorModelFactory) {
        deviceAuthApi.getDeviceAuthScreen(
            screenBody = ScreenBody(
                params = Params(serial = serial)
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun associateDevice(
        serial: String,
        code: String,
        disassociateDevice: Boolean
    ): Result<AssociateDeviceModel> = apiCall(errorModelFactory) {
        deviceAuthApi.associateDevice(
            associateDeviceBody = AssociateDeviceBody(
                serial = serial,
                code = code,
                disassociateDevice = disassociateDevice
            )
        )
    }.mapResult {
        it.mapToDomain()
    }
}
