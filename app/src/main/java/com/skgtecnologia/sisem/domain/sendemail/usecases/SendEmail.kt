package com.skgtecnologia.sisem.domain.sendemail.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.sendemail.SendEmailRepository
import javax.inject.Inject

class SendEmail @Inject constructor(
    private val repository: SendEmailRepository
) {
    suspend operator fun invoke(idAph: String, to: String, body: String): Result<Unit> = resultOf {
        repository.sendEmail(idAph, to, body)
    }
}
