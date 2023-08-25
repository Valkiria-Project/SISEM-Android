package com.skgtecnologia.sisem.domain.changepassword.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import javax.inject.Inject

class GetLoginNavigationModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val operationRepository: OperationRepository
) {

    @CheckResult
    suspend fun invoke(): Result<LoginNavigationModel> = resultOf {
        val lastAccessToken = authRepository.getLastAccessToken()
        val code = operationRepository.retrieveOperationConfig()?.vehicleCode

        LoginNavigationModel(
            isAdmin = lastAccessToken?.isAdmin == true,
            isTurnComplete = lastAccessToken?.turn?.isComplete == true,
            requiresPreOperational = lastAccessToken?.preoperational?.status == true,
            preOperationRole = OperationRole.getRoleByName(lastAccessToken?.role.orEmpty()),
            requiresDeviceAuth = code.isNullOrEmpty()
        )
    }
}
