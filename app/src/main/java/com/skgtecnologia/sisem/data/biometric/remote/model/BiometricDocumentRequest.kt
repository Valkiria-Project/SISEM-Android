package com.skgtecnologia.sisem.data.biometric.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Body for the líder APH biometric enrollment (POST v1/biometric).
 * The crew member is identified only by document number — no token/username/role is sent.
 */
@JsonClass(generateAdapter = true)
data class BiometricDocumentRequest(
    @Json(name = "documentNumber") val documentNumber: String,
    /** Base64-encoded float vectors, one entry per enrollment angle (3 total). */
    @Json(name = "embeddings") val embeddings: List<String>
)
