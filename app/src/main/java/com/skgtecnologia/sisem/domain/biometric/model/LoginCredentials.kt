package com.skgtecnologia.sisem.domain.biometric.model

/** Decrypted crew credentials used to silently re-authenticate after a biometric match. */
data class LoginCredentials(
    val username: String,
    val password: String
)
