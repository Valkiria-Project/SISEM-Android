package com.skgtecnologia.sisem.domain.loginscreen.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.loginscreen.LoginScreenRepository
import com.skgtecnologia.sisem.domain.loginscreen.model.LoginScreenModel
import javax.inject.Inject

class GetLoginScreen @Inject constructor(
		private val loginScreenRepository: LoginScreenRepository
) {

		@CheckResult
		suspend operator fun invoke(serial: String): Result<LoginScreenModel> = resultOf {
				val fetchResult = loginScreenRepository.getLoginScreen(serial)

				fetchResult
		}
}
