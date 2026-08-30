package com.skgtecnologia.sisem.data.biometric.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BiometricRequest(
    @Json(name = "username") val username: String,
    @Json(name = "role") val role: String,
    /** Base64-encoded float vectors, one entry per enrollment angle (3 total). */
    @Json(name = "embeddings") val embeddings: List<String>
)
