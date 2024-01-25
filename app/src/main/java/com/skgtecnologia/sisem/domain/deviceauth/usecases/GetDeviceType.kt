package com.skgtecnologia.sisem.domain.deviceauth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.deviceauth.DeviceAuthRepository
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceModel
import javax.inject.Inject

class GetDeviceType @Inject constructor(
    private val deviceAuthRepository: DeviceAuthRepository
) {

    @CheckResult
    suspend operator fun invoke(code: String): Result<DeviceModel> = resultOf {
        deviceAuthRepository.getDeviceType(code)
    }
}
