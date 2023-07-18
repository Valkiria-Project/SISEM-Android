package com.skgtecnologia.sisem.domain.deviceauth.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.deviceauth.DeviceAuthRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetDeviceAuthScreen @Inject constructor(
    private val deviceAuthRepository: DeviceAuthRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<ScreenModel> = resultOf {
        deviceAuthRepository.getDeviceAuthScreen(serial)
    }
}
