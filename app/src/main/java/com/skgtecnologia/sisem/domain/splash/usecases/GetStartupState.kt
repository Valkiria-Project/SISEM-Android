package com.skgtecnologia.sisem.domain.splash.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.operation.usecases.RetrieveOperationConfig
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import javax.inject.Inject

class GetStartupState @Inject constructor(
    private val authRepository: AuthRepository,
    private val retrieveOperationConfig: RetrieveOperationConfig
) {

    @CheckResult
    suspend operator fun invoke(): Result<StartupNavigationModel> = resultOf {
        val accessToken = authRepository.getLastAccessToken()
        val operationConfig = retrieveOperationConfig.invoke().getOrNull()

        StartupNavigationModel(
            isAdmin = accessToken?.isAdmin == true,
            isTurnStarted = accessToken?.turn?.isComplete == true,
            requiresPreOperational = accessToken?.preoperational?.status == true,
            preOperationRole = OperationRole.getRoleByName(accessToken?.role.orEmpty()),
            vehicleCode = operationConfig?.vehicleCode
        )
    }
}
