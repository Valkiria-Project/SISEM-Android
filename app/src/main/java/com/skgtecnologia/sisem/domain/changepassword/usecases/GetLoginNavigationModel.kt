package com.skgtecnologia.sisem.domain.changepassword.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import com.skgtecnologia.sisem.ui.navigation.model.LoginNavigationModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetLoginNavigationModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val operationRepository: OperationRepository
) {

    @CheckResult
    suspend fun invoke(): Result<LoginNavigationModel> = resultOf {
        val lastAccessToken = authRepository.observeCurrentAccessToken()
        val code = operationRepository.observeOperationConfig().first()?.vehicleCode

        LoginNavigationModel(
            isAdmin = lastAccessToken.first()?.isAdmin == true,
            isTurnComplete = lastAccessToken.first()?.turn?.isComplete == true,
            requiresPreOperational = lastAccessToken.first()?.preoperational?.status == true,
            preOperationRole = OperationRole.getRoleByName(lastAccessToken.first()?.role.orEmpty()),
            requiresDeviceAuth = code.isNullOrEmpty()
        )
    }
}
