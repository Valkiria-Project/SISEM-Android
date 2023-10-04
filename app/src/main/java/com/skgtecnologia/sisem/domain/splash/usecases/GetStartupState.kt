package com.skgtecnologia.sisem.domain.splash.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetStartupState @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeOperationConfig: ObserveOperationConfig
) {

    @CheckResult
    suspend operator fun invoke(): Result<StartupNavigationModel> = resultOf {
        val accessToken = authRepository.observeCurrentAccessToken()
        val operationConfig = observeOperationConfig.invoke().getOrNull()

        StartupNavigationModel(
            isAdmin = accessToken.first()?.isAdmin == true,
            isTurnStarted = accessToken.first()?.turn?.isComplete == true,
            requiresPreOperational = accessToken.first()?.preoperational?.status == true,
            preOperationRole = OperationRole.getRoleByName(accessToken.first()?.role.orEmpty()),
            vehicleCode = operationConfig?.vehicleCode
        )
    }
}
