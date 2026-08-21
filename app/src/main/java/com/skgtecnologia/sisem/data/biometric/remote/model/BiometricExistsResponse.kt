package com.skgtecnologia.sisem.data.biometric.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response for GET v1/biometric/exists. Lightweight existence check keyed by document
 * number: it only reports whether a record is registered, without returning embeddings.
 */
@JsonClass(generateAdapter = true)
data class BiometricExistsResponse(
    @Json(name = "registered") val registered: Boolean
)
