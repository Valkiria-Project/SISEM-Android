package com.skgtecnologia.sisem.domain.login.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.login.LoginRepository
import com.skgtecnologia.sisem.domain.login.model.LoginModel
import javax.inject.Inject

class Login @Inject constructor(
    private val loginRepository: LoginRepository
) {

    @CheckResult
    suspend operator fun invoke(username: String, password: String): Result<LoginModel> = resultOf {
        loginRepository.login(username, password)
    }
}
