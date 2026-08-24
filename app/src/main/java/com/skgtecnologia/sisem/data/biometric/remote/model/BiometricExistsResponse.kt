package com.skgtecnologia.sisem.data.biometric.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response for GET v1/biometric/exists. Existence check keyed by document number: reports
 * whether a record is registered and, when it is, the crew member's basic identity data
 * (no embeddings are returned).
 */
@JsonClass(generateAdapter = true)
data class BiometricExistsResponse(
    @Json(name = "userName") val userName: String? = null,
    @Json(name = "userLastName") val userLastName: String? = null,
    @Json(name = "documentNumber") val documentNumber: String? = null,
    @Json(name = "role") val role: String? = null,
    @Json(name = "exists") val exists: Boolean = false
)
