package com.skgtecnologia.sisem.data.sendemail.remote

import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.sendemail.remote.model.SendEmailBody
import javax.inject.Inject

class SendEmailRemoteDataSource @Inject constructor(
    private val sendEmailApi: SendEmailApi
) {

    suspend fun sendEmail(idAph: String, to: String, body: String): Result<Unit> = apiCall {
        sendEmailApi.sendEmail(
            sendEmailBody = SendEmailBody(
                idAph = idAph,
                to = to,
                body = body
            )
        )
    }
}
