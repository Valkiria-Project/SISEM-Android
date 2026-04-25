package com.skgtecnologia.sisem.domain.sendemail

interface SendEmailRepository {

    suspend fun sendEmail(idAph: String, to: String, body: String)
}
