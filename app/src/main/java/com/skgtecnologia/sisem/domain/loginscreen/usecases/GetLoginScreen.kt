package com.skgtecnologia.sisem.domain.loginscreen.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.loginscreen.LoginRepository
import com.skgtecnologia.sisem.domain.loginscreen.model.LoginScreenModel
import javax.inject.Inject

class GetLoginScreen @Inject constructor(
		private val loginRepository: LoginRepository
) {

		@CheckResult
		suspend operator fun invoke(serial: String): Result<LoginScreenModel> = resultOf {
				val fetchResult = loginRepository.getLoginScreen(serial)

				fetchResult
		}
}
