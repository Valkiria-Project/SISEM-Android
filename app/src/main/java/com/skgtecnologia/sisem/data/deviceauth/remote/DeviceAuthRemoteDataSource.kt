package com.skgtecnologia.sisem.data.deviceauth.remote

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.deviceauth.remote.model.AssociateDeviceBody
import com.skgtecnologia.sisem.data.deviceauth.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.deviceauth.model.AssociateDeviceModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class DeviceAuthRemoteDataSource @Inject constructor(
    private val deviceAuthApi: DeviceAuthApi
) {

    suspend fun getDeviceAuthScreen(serial: String): Result<ScreenModel> = resultOf {
        val response = withContext(Dispatchers.IO) {
            deviceAuthApi.getDeviceAuthScreen(
                screenBody = ScreenBody(
                    params = Params(serial = serial)
                )
            )
        }

        val body = response.body()

        if (response.isSuccessful && body != null) {
            body.mapToDomain()
        } else {
            Timber.d("The retrieved response is not successful and/or body is empty")
            error("The retrieved response is not successful and/or body is empty")
        }
    }

    suspend fun associateDevice(
        licensePlate: String,
        serial: String,
        code: String
    ): Result<AssociateDeviceModel> = resultOf {
        val response = withContext(Dispatchers.IO) {
            deviceAuthApi.associateDevice(
                associateDeviceBody = AssociateDeviceBody(
                    licensePlate = licensePlate,
                    serial = serial,
                    code = code
                )
            )
        }

        val body = response.body()

        if (response.isSuccessful && body != null) {
            body.mapToDomain()
        } else {
            Timber.d("The retrieved response is not successful and/or body is empty")
            error("The retrieved response is not successful and/or body is empty")
        }
    }
}
