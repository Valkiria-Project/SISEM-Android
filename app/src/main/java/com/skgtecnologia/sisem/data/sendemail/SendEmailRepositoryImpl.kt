package com.skgtecnologia.sisem.data.sendemail

import com.skgtecnologia.sisem.data.sendemail.remote.SendEmailRemoteDataSource
import com.skgtecnologia.sisem.domain.sendemail.SendEmailRepository
import javax.inject.Inject

class SendEmailRepositoryImpl @Inject constructor(
    private val sendEmailRemoteDataSource: SendEmailRemoteDataSource
) : SendEmailRepository {

    override suspend fun sendEmail(idAph: String, to: String, body: String) =
        sendEmailRemoteDataSource.sendEmail(
            idAph = idAph,
            to = to,
            body = body
        ).getOrThrow()
}
