package com.skgtecnologia.sisem.domain.deviceauth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.deviceauth.DeviceAuthRepository
import com.skgtecnologia.sisem.domain.deviceauth.model.AssociateDeviceModel
import javax.inject.Inject

class AssociateDevice @Inject constructor(
    private val deviceAuthRepository: DeviceAuthRepository
) {

    @CheckResult
    suspend operator fun invoke(
        serial: String,
        code: String,
        disassociateDevice: Boolean
    ): Result<AssociateDeviceModel> = resultOf {
        deviceAuthRepository.associateDevice(serial, code, disassociateDevice)
    }
}
