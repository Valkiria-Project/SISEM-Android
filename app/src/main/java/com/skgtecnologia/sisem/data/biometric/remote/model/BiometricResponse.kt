package com.skgtecnologia.sisem.data.biometric.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BiometricResponse(
    @Json(name = "username") val username: String,
    @Json(name = "role") val role: String,
    /** Base64-encoded float vectors, one per enrollment angle. */
    @Json(name = "embeddings") val embeddings: List<String>
)
