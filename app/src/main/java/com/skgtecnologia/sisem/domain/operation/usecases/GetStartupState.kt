package com.skgtecnologia.sisem.domain.operation.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.operation.model.PreoperationalStatus
import com.skgtecnologia.sisem.ui.navigation.StartupNavigationModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetStartupState @Inject constructor(
    private val authRepository: AuthRepository,
    private val getOperationConfigWithCurrentRole: GetOperationConfigWithCurrentRole
) {

    @CheckResult
    suspend operator fun invoke(): Result<StartupNavigationModel> = resultOf {
        val token = authRepository.observeCurrentAccessToken().first()
        val operationConfig = getOperationConfigWithCurrentRole.invoke().getOrNull()
        val configPreoperational = PreoperationalStatus.getStatusByName(
            operationConfig?.vehicleConfig?.preoperational.orEmpty()
        ) == PreoperationalStatus.SI

        StartupNavigationModel(
            isAdmin = token?.isAdmin == true,
            isTurnStarted = token?.turn?.isComplete == true,
            isWarning = token?.isWarning == true,
            requiresPreOperational = token?.preoperational?.status == true && configPreoperational,
            preOperationRole = OperationRole.getRoleByName(token?.role.orEmpty()),
            vehicleCode = operationConfig?.vehicleCode
        )
    }
}
